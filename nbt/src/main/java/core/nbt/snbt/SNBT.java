package core.nbt.snbt;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import core.nbt.annotation.RootName;
import core.nbt.tag.*;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SNBT {
    private @Getter Gson gson = SNBTBuilder.DEFAULT_GSON_BUILDER.create();
    private boolean serializeBooleans = false;

    private final Map<Type, Function<Tag, JsonElement>> toJson = Map.ofEntries(
            Map.entry(ByteArrayTag.class, tag -> {
                var array = new JsonArray();
                for (var v : ((ByteArrayTag) tag).getValue()) array.add(v);
                return array;
            }),
            Map.entry(ByteTag.class, tag -> {
                var value = ((ByteTag) tag).getValue();
                if (serializeBooleans && (value == 0 || value == 1))
                    return new JsonPrimitive(value == 1);
                return new JsonPrimitive(value);
            }),
            Map.entry(CompoundTag.class, tag -> {
                var object = new JsonObject();
                for (var v : ((CompoundTag) tag).entrySet()) object.add(v.getKey(), toJsonTree(v.getValue()));
                return object;
            }),
            Map.entry(DoubleTag.class, tag -> new JsonPrimitive(((DoubleTag) tag).getValue())),
            Map.entry(FloatTag.class, tag -> new JsonPrimitive(((FloatTag) tag).getValue())),
            Map.entry(IntArrayTag.class, tag -> {
                var array = new JsonArray();
                for (var v : ((IntArrayTag) tag).getValue()) array.add(v);
                return array;
            }),
            Map.entry(IntTag.class, tag -> new JsonPrimitive(((IntTag) tag).getValue())),
            Map.entry(ListTag.class, tag -> {
                var list = (ListTag<?>) tag;
                var array = new JsonArray();
                for (var v : list.getValue()) array.add(toJsonTree(v));
                return array;
            }),
            Map.entry(LongArrayTag.class, tag -> {
                var array = new JsonArray();
                for (var v : ((LongArrayTag) tag).getValue()) array.add(v);
                return array;
            }),
            Map.entry(LongTag.class, tag -> new JsonPrimitive(((LongTag) tag).getValue())),
            Map.entry(ShortTag.class, tag -> new JsonPrimitive(((ShortTag) tag).getValue())),
            Map.entry(StringTag.class, tag -> new JsonPrimitive(((StringTag) tag).getValue()))
    );

    private final Map<Type, Function<JsonElement, Tag>> fromJson = Map.ofEntries(
            Map.entry(JsonPrimitive.class, element -> {
                var primitive = element.getAsJsonPrimitive();
                var tagType = getTagType(primitive);
                return switch (tagType) {
                    case STRING -> new StringTag(primitive.getAsString());
                    case BOOLEAN -> new ByteTag(primitive.getAsBoolean());
                    case INTEGER -> new IntTag(primitive.getAsInt());
                    case LONG -> new LongTag(primitive.getAsLong());
                    case FLOAT -> new FloatTag(primitive.getAsFloat());
                    case DOUBLE -> new DoubleTag(primitive.getAsDouble());
                    case BYTE -> new ByteTag(primitive.getAsByte());
                    case SHORT -> new ShortTag(primitive.getAsShort());
                    default -> throw new IllegalStateException("Unexpected value: " + tagType);
                };
            }),
            Map.entry(JsonObject.class, element -> {
                var object = element.getAsJsonObject();
                var tag = new CompoundTag();
                object.entrySet().forEach(entry -> {
                    var serialize = fromJson(entry.getValue());
                    serialize.setName(entry.getKey());
                    tag.put(entry.getKey(), serialize);
                });
                return tag;
            }),
            Map.entry(JsonArray.class, element -> {
                var array = element.getAsJsonArray();
                return switch (getArrayType(array)) {
                    case BYTE -> {
                        var v = new byte[array.size()];
                        for (var i = 0; i < array.size(); i++) v[i] = array.get(i).getAsByte();
                        yield new ByteArrayTag(v);
                    }
                    case INT -> {
                        var v = new int[array.size() - 1];
                        for (var i = 0; i < array.size(); i++) v[i] = array.get(i).getAsInt();
                        yield new IntArrayTag(v);
                    }
                    case LIST -> {
                        var content = new ArrayList<Tag>();
                        for (var i = 0; i < array.size(); i++) content.add(fromJson(array.get(i)));
                        yield new ListTag<>(content, getContentType(array).getId());
                    }
                    case LONG -> {
                        var v = new long[array.size() - 1];
                        for (var i = 0; i < array.size(); i++) v[i] = array.get(i).getAsLong();
                        yield new LongArrayTag(v);
                    }
                };
            })
    );

    public JsonElement toJsonTree(Tag tag) {
        return toJson.get(tag.getClass()).apply(tag);
    }

    public Tag toTag(Object object, Type type) {
        var annotation = getAnnotation(type, RootName.class);
        var tag = fromJson(getGson().toJsonTree(object, type));
        return annotation != null ? wrap(annotation, tag) : tag;
    }

    public <T> T fromTag(Tag tag, Type type) {
        var annotation = getAnnotation(type, RootName.class);
        var wrapped = annotation != null ? wrap(annotation, tag) : tag;
        return getGson().fromJson(toJsonTree(wrapped), type);
    }

    private Tag wrap(RootName annotation, Tag tag) {
        var compoundTag = new CompoundTag(annotation.value());
        tag.setName(annotation.value());
        compoundTag.add(tag);
        return compoundTag;
    }

    private @Nullable <A extends Annotation> A getAnnotation(Type type, Class<A> annotation) {
        Class<?> clazz = null;
        if (type instanceof TypeToken<?> token) clazz = token.getRawType();
        else if (type instanceof Class<?> aClass) clazz = aClass;
        return clazz != null ? clazz.getAnnotation(annotation) : null;
    }

    public Tag fromJson(JsonElement element) {
        return fromJson.get(element.getClass()).apply(element);
    }

    public TagType getContentType(JsonArray array) {
        if (array.isEmpty()) return TagType.ESCAPE;
        return getTagType(array.get(0));
    }

    public ArrayType getArrayType(JsonArray array) {
        if (array.isEmpty() || !array.isJsonPrimitive()) return ArrayType.LIST;
        return switch (getTagType(array.get(0))) {
            case BYTE -> ArrayType.BYTE;
            case INTEGER -> ArrayType.INT;
            case LONG -> ArrayType.LONG;
            default -> ArrayType.LIST;
        };
    }

    public TagType getTagType(JsonElement element) {
        if (element.isJsonObject()) return TagType.COMPOUND;
        if (element.isJsonArray()) return getArrayType(element.getAsJsonArray()).getTagType();
        var primitive = element.getAsJsonPrimitive();
        if (primitive.isNumber()) {
            var number = primitive.getAsNumber();
            System.out.println(number.getClass().getName());
            if (number instanceof Byte) return TagType.BYTE;
            if (number instanceof Short) return TagType.SHORT;
            if (number instanceof Integer) return TagType.INTEGER;
            if (number instanceof Long) return TagType.LONG;
            if (number instanceof Float) return TagType.FLOAT;
            if (number instanceof Double) return TagType.DOUBLE;
        } else if (primitive.isBoolean()) return TagType.BOOLEAN;
        return TagType.STRING;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ArrayType {
        LIST(TagType.LIST),
        BYTE(TagType.BYTE_ARRAY),
        INT(TagType.INT_ARRAY),
        LONG(TagType.LONG_ARRAY);

        private final TagType tagType;
    }

    @Getter
    @RequiredArgsConstructor
    public enum TagType {
        ESCAPE(EscapeTag.ID),
        COMPOUND(CompoundTag.ID),
        STRING(StringTag.ID),
        BOOLEAN(ByteTag.ID),
        INTEGER(IntTag.ID),
        LONG(LongTag.ID),
        FLOAT(FloatTag.ID),
        DOUBLE(DoubleTag.ID),
        BYTE(ByteTag.ID),
        SHORT(ShortTag.ID),
        LIST(ListTag.ID),
        BYTE_ARRAY(ByteArrayTag.ID),
        INT_ARRAY(IntArrayTag.ID),
        LONG_ARRAY(LongArrayTag.ID);

        private final int id;
    }

}

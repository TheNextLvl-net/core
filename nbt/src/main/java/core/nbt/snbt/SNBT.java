package core.nbt.snbt;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import core.nbt.tag.*;
import lombok.*;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SNBT {
    private Gson gson = SNBTBuilder.DEFAULT_GSON_BUILDER.create();

    /**
     * Serialize the object of the specified type into a tag
     *
     * @param object the object to serialize
     * @param type   the type of the object
     * @return the serialized tag
     */
    public Tag toTag(Object object, Type type) {
        return fromJson(gson.toJsonTree(object, type));
    }

    /**
     * Serialize the object into a tag
     *
     * @param object the object to serialize
     * @return the serialized tag
     */
    public Tag toTag(Object object) {
        return toTag(object, object.getClass());
    }

    /**
     * Deserialize the tag into an object of the specified type
     *
     * @param tag  the tag to deserialize
     * @param type the type of the object
     * @param <T>  the generic type of the object
     * @return an object of the specified type
     */
    public <T> T fromTag(Tag tag, Type type) {
        return gson.fromJson(toJsonTree(tag), type);
    }

    /**
     * Deserialize the tag into an object of the specified type
     *
     * @param tag  the tag to deserialize
     * @param type the type of the object
     * @param <T>  the generic type of the object
     * @return an object of the specified type
     */
    public <T> T fromTag(Tag tag, Class<T> type) {
        return gson.fromJson(toJsonTree(tag), type);
    }

    /**
     * Deserialize the tag into an object of the specified type
     *
     * @param tag       the tag to deserialize
     * @param typeToken the type of the object
     * @param <T>       the generic type of the object
     * @return an object of the specified type
     */
    public <T> T fromTag(Tag tag, TypeToken<T> typeToken) {
        return gson.fromJson(toJsonTree(tag), typeToken);
    }

    private JsonElement toJsonTree(Tag tag) {
        return gson.toJsonTree(tag);
    }

    private Tag fromJson(JsonElement element) {
        return gson.fromJson(element, Tag.class);
    }

    @ApiStatus.Internal
    public static TagType guessContentType(JsonArray array) {
        if (array.isEmpty()) return TagType.ESCAPE;
        return guessTagType(array.get(0));
    }

    @ApiStatus.Internal
    public static ArrayType guessArrayType(JsonArray array) {
        if (array.isEmpty() || !array.isJsonPrimitive()) return ArrayType.LIST;
        return switch (guessTagType(array.get(0))) {
            case BYTE -> ArrayType.BYTE;
            case INTEGER -> ArrayType.INT;
            case LONG -> ArrayType.LONG;
            default -> ArrayType.LIST;
        };
    }

    @ApiStatus.Internal
    public static TagType guessTagType(JsonElement element) {
        if (element.isJsonObject()) return TagType.COMPOUND;
        if (element.isJsonArray()) return guessArrayType(element.getAsJsonArray()).getTagType();
        var primitive = element.getAsJsonPrimitive();
        if (primitive.isNumber()) {
            var number = primitive.getAsNumber();
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
    @ApiStatus.Internal
    @RequiredArgsConstructor
    public enum ArrayType {
        BYTE(TagType.BYTE_ARRAY),
        INT(TagType.INT_ARRAY),
        LIST(TagType.LIST),
        LONG(TagType.LONG_ARRAY);

        private final TagType tagType;
    }

    @Getter
    @ApiStatus.Internal
    @RequiredArgsConstructor
    public enum TagType {
        BOOLEAN(ByteTag.ID),
        BYTE(ByteTag.ID),
        BYTE_ARRAY(ByteArrayTag.ID),
        COMPOUND(CompoundTag.ID),
        DOUBLE(DoubleTag.ID),
        ESCAPE(EscapeTag.ID),
        FLOAT(FloatTag.ID),
        INTEGER(IntTag.ID),
        INT_ARRAY(IntArrayTag.ID),
        LIST(ListTag.ID),
        LONG(LongTag.ID),
        LONG_ARRAY(LongArrayTag.ID),
        SHORT(ShortTag.ID),
        STRING(StringTag.ID);

        private final int id;
    }
}

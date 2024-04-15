package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.snbt.SNBT;
import core.nbt.tag.*;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
@Deprecated(forRemoval = true)
public class TagAdapter implements JsonSerializer<Tag>, JsonDeserializer<Tag> {
    @Override
    public Tag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        var tagType = SNBT.guessTagType(element);
        return switch (tagType) {
            case BOOLEAN -> context.deserialize(element, BooleanTag.class);
            case BYTE -> context.deserialize(element, ByteTag.class);
            case BYTE_ARRAY -> context.deserialize(element, ByteArrayTag.class);
            case COMPOUND -> context.deserialize(element, CompoundTag.class);
            case DOUBLE -> context.deserialize(element, DoubleTag.class);
            case FLOAT -> context.deserialize(element, FloatTag.class);
            case INTEGER -> context.deserialize(element, IntTag.class);
            case INT_ARRAY -> context.deserialize(element, IntArrayTag.class);
            case LIST -> context.deserialize(element, ListTag.class);
            case LONG -> context.deserialize(element, LongTag.class);
            case LONG_ARRAY -> context.deserialize(element, LongArrayTag.class);
            case SHORT -> context.deserialize(element, ShortTag.class);
            case STRING -> context.deserialize(element, StringTag.class);
            default -> throw new IllegalStateException("Unexpected value: " + tagType);
        };
    }

    @Override
    public JsonElement serialize(Tag tag, Type type, JsonSerializationContext context) {
        return switch (tag.getTypeId()) {
            case ByteArrayTag.ID -> context.serialize(tag, ByteArrayTag.class);
            case ByteTag.ID -> context.serialize(tag, ByteTag.class);
            case CompoundTag.ID -> context.serialize(tag, CompoundTag.class);
            case DoubleTag.ID -> context.serialize(tag, DoubleTag.class);
            case FloatTag.ID -> context.serialize(tag, FloatTag.class);
            case IntArrayTag.ID -> context.serialize(tag, IntArrayTag.class);
            case IntTag.ID -> context.serialize(tag, IntTag.class);
            case ListTag.ID -> context.serialize(tag, ListTag.class);
            case LongArrayTag.ID -> context.serialize(tag, LongArrayTag.class);
            case LongTag.ID -> context.serialize(tag, LongTag.class);
            case ShortTag.ID -> context.serialize(tag, ShortTag.class);
            case StringTag.ID -> context.serialize(tag, StringTag.class);
            default -> throw new IllegalStateException("Unexpected value: " + tag.getTypeId());
        };
    }
}
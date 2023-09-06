package core.nbt.adapter;

import com.google.gson.*;
import core.nbt.tag.*;

import java.lang.reflect.Type;

public class TagAdapter implements JsonSerializer<Tag>, JsonDeserializer<Tag> {
    @Override
    public Tag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (element.isJsonNull()) return EscapeTag.INSTANCE;
        if (!element.isJsonObject()) return null;
        var object = element.getAsJsonObject();
        if (!object.has("type-id")) return null;
        int typeId = object.get("type-id").getAsInt();
        return context.deserialize(element, switch (typeId) {
            case ByteArrayTag.ID -> ByteArrayTag.class;
            case ByteTag.ID -> ByteTag.class;
            case CompoundTag.ID -> CompoundTag.class;
            case DoubleTag.ID -> DoubleTag.class;
            case EscapeTag.ID -> EscapeTag.class;
            case FloatTag.ID -> FloatTag.class;
            case IntArrayTag.ID -> IntArrayTag.class;
            case IntTag.ID -> IntTag.class;
            case ListTag.ID -> ListTag.class;
            case LongArrayTag.ID -> LongArrayTag.class;
            case LongTag.ID -> LongTag.class;
            case ShortTag.ID -> ShortTag.class;
            case StringTag.ID -> StringTag.class;
            default -> throw new IllegalStateException("Unexpected value: " + typeId);
        });
    }

    @Override
    public JsonElement serialize(Tag tag, Type type, JsonSerializationContext context) {
        return context.serialize(tag, switch (tag.getTypeId()) {
            case ByteArrayTag.ID -> ByteArrayTag.class;
            case ByteTag.ID -> ByteTag.class;
            case CompoundTag.ID -> CompoundTag.class;
            case DoubleTag.ID -> DoubleTag.class;
            case EscapeTag.ID -> EscapeTag.class;
            case FloatTag.ID -> FloatTag.class;
            case IntArrayTag.ID -> IntArrayTag.class;
            case IntTag.ID -> IntTag.class;
            case ListTag.ID -> ListTag.class;
            case LongArrayTag.ID -> LongArrayTag.class;
            case LongTag.ID -> LongTag.class;
            case ShortTag.ID -> ShortTag.class;
            case StringTag.ID -> StringTag.class;
            default -> throw new IllegalStateException("Unexpected value: " + tag.getTypeId());
        });
    }
}

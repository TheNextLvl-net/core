package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.tag.IntTag;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
public class IntTagAdapter implements JsonSerializer<IntTag>, JsonDeserializer<IntTag> {
    @Override
    public IntTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new IntTag(element.getAsInt());
    }

    @Override
    public JsonPrimitive serialize(IntTag tag, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(tag.getAsInt());
    }
}

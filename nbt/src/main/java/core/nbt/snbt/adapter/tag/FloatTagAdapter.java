package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.tag.FloatTag;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
@Deprecated(forRemoval = true)
public class FloatTagAdapter implements JsonSerializer<FloatTag>, JsonDeserializer<FloatTag> {
    @Override
    public FloatTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new FloatTag(element.getAsFloat());
    }

    @Override
    public JsonPrimitive serialize(FloatTag tag, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(tag.getAsFloat());
    }
}

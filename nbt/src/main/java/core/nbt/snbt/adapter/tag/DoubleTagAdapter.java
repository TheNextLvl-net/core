package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.tag.DoubleTag;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
@Deprecated(forRemoval = true)
public class DoubleTagAdapter implements JsonSerializer<DoubleTag>, JsonDeserializer<DoubleTag> {
    @Override
    public DoubleTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new DoubleTag(element.getAsDouble());
    }

    @Override
    public JsonPrimitive serialize(DoubleTag tag, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(tag.getAsDouble());
    }
}

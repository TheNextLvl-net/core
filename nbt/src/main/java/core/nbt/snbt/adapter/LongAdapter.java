package core.nbt.snbt.adapter;

import com.google.gson.*;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
public class LongAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {
    @Override
    public Long deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return element.getAsJsonPrimitive().getAsLong();
    }

    @Override
    public JsonPrimitive serialize(Long value, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(value);
    }
}

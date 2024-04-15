package core.nbt.snbt.adapter.primitive;

import com.google.gson.*;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
@Deprecated(forRemoval = true)
public class FloatAdapter implements JsonSerializer<Float>, JsonDeserializer<Float> {
    @Override
    public Float deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return element.getAsJsonPrimitive().getAsFloat();
    }

    @Override
    public JsonPrimitive serialize(Float value, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(value);
    }
}

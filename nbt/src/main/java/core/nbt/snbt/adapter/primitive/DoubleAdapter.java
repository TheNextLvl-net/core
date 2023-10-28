package core.nbt.snbt.adapter.primitive;

import com.google.gson.*;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
public class DoubleAdapter implements JsonSerializer<Double>, JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return element.getAsJsonPrimitive().getAsDouble();
    }

    @Override
    public JsonPrimitive serialize(Double value, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(value);
    }
}

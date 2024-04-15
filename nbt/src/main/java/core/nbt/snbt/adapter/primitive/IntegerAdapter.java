package core.nbt.snbt.adapter.primitive;

import com.google.gson.*;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
@Deprecated(forRemoval = true)
public class IntegerAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return element.getAsJsonPrimitive().getAsInt();
    }

    @Override
    public JsonPrimitive serialize(Integer value, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(value);
    }
}

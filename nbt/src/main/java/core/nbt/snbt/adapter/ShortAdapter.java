package core.nbt.snbt.adapter;

import com.google.gson.*;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
public class ShortAdapter implements JsonSerializer<Short>, JsonDeserializer<Short> {
    @Override
    public Short deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return element.getAsJsonPrimitive().getAsShort();
    }

    @Override
    public JsonPrimitive serialize(Short value, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(value);
    }
}

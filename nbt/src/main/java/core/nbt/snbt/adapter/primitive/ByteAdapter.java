package core.nbt.snbt.adapter.primitive;

import com.google.gson.*;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
@Deprecated(forRemoval = true)
public class ByteAdapter implements JsonSerializer<Byte>, JsonDeserializer<Byte> {
    @Override
    public Byte deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return element.getAsJsonPrimitive().getAsByte();
    }

    @Override
    public JsonPrimitive serialize(Byte value, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(value);
    }
}

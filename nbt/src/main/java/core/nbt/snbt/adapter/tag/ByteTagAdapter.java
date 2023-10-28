package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.tag.ByteTag;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
public class ByteTagAdapter implements JsonSerializer<ByteTag>, JsonDeserializer<ByteTag> {
    @Override
    public ByteTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new ByteTag(element.getAsByte());
    }

    @Override
    public JsonPrimitive serialize(ByteTag tag, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(tag.getAsByte());
    }
}

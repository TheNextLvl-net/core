package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.tag.ShortTag;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
public class ShortTagAdapter implements JsonSerializer<ShortTag>, JsonDeserializer<ShortTag> {
    @Override
    public ShortTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new ShortTag(element.getAsShort());
    }

    @Override
    public JsonPrimitive serialize(ShortTag tag, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(tag.getAsShort());
    }
}

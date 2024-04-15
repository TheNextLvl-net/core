package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.tag.StringTag;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
@Deprecated(forRemoval = true)
public class StringTagAdapter implements JsonSerializer<StringTag>, JsonDeserializer<StringTag> {
    @Override
    public StringTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new StringTag(element.getAsString());
    }

    @Override
    public JsonPrimitive serialize(StringTag tag, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(tag.getAsString());
    }
}

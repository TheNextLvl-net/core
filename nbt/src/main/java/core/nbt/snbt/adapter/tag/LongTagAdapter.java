package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.tag.LongTag;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
@Deprecated(forRemoval = true)
public class LongTagAdapter implements JsonSerializer<LongTag>, JsonDeserializer<LongTag> {
    @Override
    public LongTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new LongTag(element.getAsLong());
    }

    @Override
    public JsonPrimitive serialize(LongTag tag, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(tag.getAsLong());
    }
}

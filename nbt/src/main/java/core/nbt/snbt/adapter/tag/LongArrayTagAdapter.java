package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.tag.LongArrayTag;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
@Deprecated(forRemoval = true)
public class LongArrayTagAdapter implements JsonSerializer<LongArrayTag>, JsonDeserializer<LongArrayTag> {
    @Override
    public LongArrayTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        var array = element.getAsJsonArray();
        var value = new long[array.size()];
        for (int i = 0; i < array.size(); i++) value[i] = array.get(i).getAsLong();
        return new LongArrayTag(value);
    }

    @Override
    public JsonArray serialize(LongArrayTag tag, Type type, JsonSerializationContext context) {
        var array = new JsonArray(tag.size());
        for (int i = 0; i < tag.size(); i++) array.set(i, new JsonPrimitive(tag.get(i)));
        return array;
    }
}

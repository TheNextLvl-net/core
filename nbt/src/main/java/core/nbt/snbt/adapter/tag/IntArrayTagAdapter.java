package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.tag.IntArrayTag;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
public class IntArrayTagAdapter implements JsonSerializer<IntArrayTag>, JsonDeserializer<IntArrayTag> {
    @Override
    public IntArrayTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        var array = element.getAsJsonArray();
        var value = new int[array.size()];
        for (int i = 0; i < array.size(); i++) value[i] = array.get(i).getAsInt();
        return new IntArrayTag(value);
    }

    @Override
    public JsonArray serialize(IntArrayTag tag, Type type, JsonSerializationContext context) {
        var array = new JsonArray(tag.size());
        for (int i = 0; i < tag.size(); i++) array.set(i, new JsonPrimitive(tag.get(i)));
        return array;
    }
}

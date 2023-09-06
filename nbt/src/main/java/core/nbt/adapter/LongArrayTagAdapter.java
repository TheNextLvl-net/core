package core.nbt.adapter;

import com.google.gson.*;
import core.nbt.tag.LongArrayTag;

import java.lang.reflect.Type;
import java.util.Optional;

public class LongArrayTagAdapter implements JsonSerializer<LongArrayTag>, JsonDeserializer<LongArrayTag> {
    @Override
    public LongArrayTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!element.isJsonObject()) return null;
        var object = element.getAsJsonObject();
        var array = object.getAsJsonArray("value");
        var name = Optional.ofNullable(object.get("name"))
                .map(json -> json.isJsonNull() ? null : json)
                .map(JsonElement::getAsString)
                .orElse(null);
        var value = new long[array.size()];
        for (int i = 0; i < array.size(); i++) value[i] = array.get(i).getAsLong();
        return new LongArrayTag(name, value);
    }

    @Override
    public JsonObject serialize(LongArrayTag tag, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        var value = new JsonArray();
        for (var l : tag.getLongs()) value.add(l);
        object.addProperty("name", tag.getName());
        object.addProperty("type-id", tag.getTypeId());
        object.add("value", value);
        return object;
    }
}

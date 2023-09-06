package core.nbt.adapter;

import com.google.gson.*;
import core.nbt.tag.IntArrayTag;

import java.lang.reflect.Type;
import java.util.Optional;

public class IntArrayTagAdapter implements JsonSerializer<IntArrayTag>, JsonDeserializer<IntArrayTag> {
    @Override
    public IntArrayTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!element.isJsonObject()) return null;
        var object = element.getAsJsonObject();
        var array = object.getAsJsonArray("value");
        var name = Optional.ofNullable(object.get("name"))
                .map(json -> json.isJsonNull() ? null : json)
                .map(JsonElement::getAsString)
                .orElse(null);
        var value = new int[array.size()];
        for (int i = 0; i < array.size(); i++) value[i] = array.get(i).getAsInt();
        return new IntArrayTag(name, value);
    }

    @Override
    public JsonObject serialize(IntArrayTag tag, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        var value = new JsonArray();
        for (var i : tag.getValue()) value.add(i);
        object.addProperty("name", tag.getName());
        object.addProperty("type-id", tag.getTypeId());
        object.add("value", value);
        return object;
    }
}

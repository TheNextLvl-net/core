package core.nbt.adapter;

import com.google.gson.*;
import core.nbt.tag.DoubleTag;

import java.lang.reflect.Type;
import java.util.Optional;

public class DoubleTagAdapter implements JsonSerializer<DoubleTag>, JsonDeserializer<DoubleTag> {
    @Override
    public DoubleTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!element.isJsonObject()) return null;
        var object = element.getAsJsonObject();
        var name = Optional.ofNullable(object.get("name"))
                .map(json -> json.isJsonNull() ? null : json)
                .map(JsonElement::getAsString)
                .orElse(null);
        var value = object.get("value").getAsDouble();
        return new DoubleTag(name, value);
    }

    @Override
    public JsonObject serialize(DoubleTag tag, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("name", tag.getName());
        object.addProperty("type-id", tag.getTypeId());
        object.addProperty("value", tag.getValue());
        return object;
    }
}

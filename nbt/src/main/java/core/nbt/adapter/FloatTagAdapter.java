package core.nbt.adapter;

import com.google.gson.*;
import core.nbt.tag.FloatTag;

import java.lang.reflect.Type;
import java.util.Optional;

public class FloatTagAdapter implements JsonSerializer<FloatTag>, JsonDeserializer<FloatTag> {
    @Override
    public FloatTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!element.isJsonObject()) return null;
        var object = element.getAsJsonObject();
        var name = Optional.ofNullable(object.get("name"))
                .map(json -> json.isJsonNull() ? null : json)
                .map(JsonElement::getAsString)
                .orElse(null);
        var value = object.get("value").getAsFloat();
        return new FloatTag(name, value);
    }

    @Override
    public JsonObject serialize(FloatTag tag, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("name", tag.getName());
        object.addProperty("type-id", tag.getTypeId());
        object.addProperty("value", tag.getValue());
        return object;
    }
}

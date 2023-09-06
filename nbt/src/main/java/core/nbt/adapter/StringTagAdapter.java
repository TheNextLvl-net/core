package core.nbt.adapter;

import com.google.gson.*;
import core.nbt.tag.StringTag;

import java.lang.reflect.Type;
import java.util.Optional;

public class StringTagAdapter implements JsonSerializer<StringTag>, JsonDeserializer<StringTag> {
    @Override
    public StringTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!element.isJsonObject()) return null;
        var object = element.getAsJsonObject();
        var name = Optional.ofNullable(object.get("name"))
                .map(json -> json.isJsonNull() ? null : json)
                .map(JsonElement::getAsString)
                .orElse(null);
        var value = object.get("value").getAsString();
        return new StringTag(name, value);
    }

    @Override
    public JsonObject serialize(StringTag tag, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("name", tag.getName());
        object.addProperty("type-id", tag.getTypeId());
        object.addProperty("value", tag.getValue());
        return object;
    }
}

package core.nbt.adapter;

import com.google.gson.*;
import core.nbt.tag.ByteArrayTag;

import java.lang.reflect.Type;
import java.util.Optional;

public class ByteArrayTagAdapter implements JsonSerializer<ByteArrayTag>, JsonDeserializer<ByteArrayTag> {
    @Override
    public ByteArrayTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!element.isJsonObject()) return null;
        var object = element.getAsJsonObject();
        var array = object.getAsJsonArray("value");
        var name = Optional.ofNullable(object.get("name"))
                .map(json -> json.isJsonNull() ? null : json)
                .map(JsonElement::getAsString)
                .orElse(null);
        var bytes = new byte[array.size()];
        for (int i = 0; i < array.size(); i++) bytes[i] = array.get(i).getAsByte();
        return new ByteArrayTag(name, bytes);
    }

    @Override
    public JsonObject serialize(ByteArrayTag tag, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        var value = new JsonArray();
        for (byte b : tag.getBytes()) value.add(b);
        object.addProperty("name", tag.getName());
        object.addProperty("type-id", tag.getTypeId());
        object.add("value", value);
        return object;
    }
}

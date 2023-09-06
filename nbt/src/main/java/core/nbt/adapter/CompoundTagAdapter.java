package core.nbt.adapter;

import com.google.gson.*;
import core.nbt.tag.CompoundTag;
import core.nbt.tag.Tag;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Optional;

public class CompoundTagAdapter implements JsonSerializer<CompoundTag>, JsonDeserializer<CompoundTag> {
    @Override
    public CompoundTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!element.isJsonObject()) return null;
        var object = element.getAsJsonObject();
        var array = object.getAsJsonArray("value");
        var name = Optional.ofNullable(object.get("name"))
                .map(json -> json.isJsonNull() ? null : json)
                .map(JsonElement::getAsString)
                .orElse(null);
        var map = new HashMap<String, Tag>();
        array.forEach(entry -> {
            var tag = context.<Tag>deserialize(entry, Tag.class);
            map.put(tag.getName(), tag);
        });
        return new CompoundTag(name, map);
    }

    @Override
    public JsonObject serialize(CompoundTag tag, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        var array = new JsonArray();
        tag.getMap().forEach((name, value) -> array.add(context.serialize(value)));
        object.addProperty("name", tag.getName());
        object.addProperty("type-id", tag.getTypeId());
        object.add("value", array);
        return object;
    }
}

package core.nbt.adapter;

import com.google.gson.*;
import core.nbt.tag.ListTag;
import core.nbt.tag.Tag;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;

public class ListTagAdapter implements JsonSerializer<ListTag<?>>, JsonDeserializer<ListTag<?>> {
    @Override
    public ListTag<?> deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!element.isJsonObject()) return null;
        var object = element.getAsJsonObject();
        var array = object.getAsJsonArray("value");
        var name = Optional.ofNullable(object.get("name"))
                .map(json -> json.isJsonNull() ? null : json)
                .map(JsonElement::getAsString)
                .orElse(null);
        var contentTypeId = object.get("content-type-id").getAsInt();
        var value = new ArrayList<Tag>();
        array.forEach(json -> value.add(context.deserialize(json, Tag.class)));
        return new ListTag<>(name, value, contentTypeId);
    }

    @Override
    public JsonObject serialize(ListTag<?> tag, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        var value = new JsonArray();
        tag.getCollection().forEach(tag1 -> value.add(context.serialize(tag1)));
        object.addProperty("name", tag.getName());
        object.addProperty("type-id", tag.getTypeId());
        object.addProperty("content-type-id", tag.getContentTypeId());
        object.add("value", value);
        return object;
    }
}

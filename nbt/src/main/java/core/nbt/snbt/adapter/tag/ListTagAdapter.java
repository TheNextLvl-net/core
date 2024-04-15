package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.snbt.SNBT;
import core.nbt.tag.ListTag;
import core.nbt.tag.Tag;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ApiStatus.Internal
@Deprecated(forRemoval = true)
public class ListTagAdapter implements JsonSerializer<ListTag<?>>, JsonDeserializer<ListTag<?>> {
    @Override
    public ListTag<?> deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        var array = element.getAsJsonArray();
        var content = new ArrayList<Tag>();
        for (var i = 0; i < array.size(); i++) content.add(context.deserialize(array.get(i), Tag.class));
        return new ListTag<>(content, SNBT.guessContentType(array).getId());
    }

    @Override
    public JsonArray serialize(ListTag<?> tag, Type type, JsonSerializationContext context) {
        var array = new JsonArray();
        tag.forEach(v -> array.add(context.serialize(v)));
        return array;
    }
}

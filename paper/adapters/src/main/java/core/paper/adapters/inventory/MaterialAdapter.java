package core.paper.adapters.inventory;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * This adapter uses the material key for de/serialization<br>
 * Right now it is just a user-friendly way of working with materials<br>
 * <i>This adapter is not required since {@link Material} is an enum, but it may be in the future</i>
 */
public class MaterialAdapter extends PaperAdapter<Material> {

    @Override
    public @Nullable Material deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return Material.matchMaterial(element.getAsString());
    }

    @Override
    public JsonElement serialize(Material source, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(source.getKey().toString());
    }
}

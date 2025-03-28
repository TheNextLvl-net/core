package core.paper.adapters.inventory;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * This adapter uses the material key for de/serialization.
 */
@NullMarked
public final class MaterialAdapter implements PaperAdapter<Material> {
    public static MaterialAdapter instance() {
        return new MaterialAdapter();
    }

    @Override
    public @Nullable Material deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return element.isJsonNull() ? null : Material.matchMaterial(element.getAsString());
    }

    @Override
    public JsonElement serialize(@Nullable Material source, Type type, JsonSerializationContext context) {
        return source == null ? JsonNull.INSTANCE : new JsonPrimitive(source.getKey().toString());
    }
}

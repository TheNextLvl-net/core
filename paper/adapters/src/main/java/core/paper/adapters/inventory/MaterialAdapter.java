package core.paper.adapters.inventory;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.lang.reflect.Type;

/**
 * This adapter uses the material key for de/serialization<br>
 * Right now it is just a user-friendly way of working with materials<br>
 * <i>This adapter is not required since {@link Material} is an enum, but it may be in the future</i>
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MaterialAdapter extends PaperAdapter<Material> {

    /**
     * This adapter deserializes non-existing or invalid materials into null
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Nullable extends MaterialAdapter {
        public static final Nullable INSTANCE = new Nullable();

        @Override
        @org.jetbrains.annotations.Nullable
        public Material deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return Material.matchMaterial(element.getAsString());
        }
    }

    /**
     * This adapter throws an exception when confronted with non-existing or invalid materials
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NotNull extends MaterialAdapter {
        public static final MaterialAdapter INSTANCE = new NotNull();

        @Override
        public Material deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            var material = Material.matchMaterial(element.getAsString());
            Preconditions.checkNotNull(material, "Invalid material: " + element.getAsString());
            return material;
        }
    }

    @Override
    public JsonElement serialize(Material source, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(source.getKey().toString());
    }
}

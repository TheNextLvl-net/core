package core.paper.adapters.world;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public abstract class WorldAdapter extends PaperAdapter<World> {

    /**
     * @see Bukkit#getWorld(java.util.UUID)
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UUID extends WorldAdapter {
        public static final WorldAdapter INSTANCE = new UUID();

        @Override
        public @Nullable World deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return Bukkit.getWorld(java.util.UUID.fromString(element.getAsString()));
        }

        @Override
        public JsonElement serialize(World source, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(source.getUID().toString());
        }
    }

    /**
     * @see Bukkit#getWorld(String)
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Name extends WorldAdapter {
        public static final WorldAdapter INSTANCE = new Name();

        @Override
        public @Nullable World deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return Bukkit.getWorld(element.getAsString());
        }

        @Override
        public JsonElement serialize(World source, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(source.getName());
        }
    }

    /**
     * @see Bukkit#getWorld(NamespacedKey)
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Key extends WorldAdapter {
        public static final WorldAdapter INSTANCE = new Key();

        @Override
        public @Nullable World deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            var key = NamespacedKey.fromString(element.getAsString());
            return key != null ? Bukkit.getWorld(key) : null;
        }

        @Override
        public JsonElement serialize(World source, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(source.getKey().toString());
        }
    }
}
package core.paper.adapters.world;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * This adapter provides various adapters for world de/serialization
 */
@NullMarked
public final class WorldAdapter {
    public static UUID uuid() {
        return new UUID();
    }

    public static Name name() {
        return new Name();
    }

    public static Key key() {
        return new Key();
    }

    /**
     * This adapter uses the uuid of the world for de/serialization
     *
     * @see Bukkit#getWorld(java.util.UUID)
     */
    public static final class UUID implements PaperAdapter<World> {
        @Override
        @NullUnmarked
        public @Nullable World deserialize(@NonNull JsonElement element, @NonNull Type type, @NonNull JsonDeserializationContext context) throws JsonParseException {
            var uuid = context.<java.util.UUID>deserialize(element, java.util.UUID.class);
            return uuid != null ? Bukkit.getWorld(uuid) : null;
        }

        @Override
        public JsonElement serialize(@Nullable World source, Type type, JsonSerializationContext context) {
            return source != null ? context.serialize(source.getUID()) : JsonNull.INSTANCE;
        }
    }

    /**
     * This adapter uses the name of the world for de/serialization
     *
     * @see Bukkit#getWorld(String)
     */
    public static final class Name implements PaperAdapter<World> {
        @Override
        public @Nullable World deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return element.isJsonNull() ? null : Bukkit.getWorld(element.getAsString());
        }

        @Override
        public JsonElement serialize(@Nullable World source, Type type, JsonSerializationContext context) {
            return source != null ? new JsonPrimitive(source.getName()) : JsonNull.INSTANCE;
        }
    }

    /**
     * This adapter uses the key of the world for de/serialization
     *
     * @see Bukkit#getWorld(NamespacedKey)
     */
    public static final class Key implements PaperAdapter<World> {
        @Override
        public @Nullable World deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (!element.isJsonPrimitive()) return null;
            var key = NamespacedKey.fromString(element.getAsString());
            return key != null ? Bukkit.getWorld(key) : null;
        }

        @Override
        public JsonElement serialize(@Nullable World source, Type type, JsonSerializationContext context) {
            return source != null ? new JsonPrimitive(source.getKey().asString()) : JsonNull.INSTANCE;
        }
    }
}

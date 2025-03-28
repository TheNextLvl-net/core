package core.paper.adapters.player;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import core.paper.adapters.api.PaperAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * This class provides various adapters for offline-player de/serialization
 */
@NullMarked
public final class OfflinePlayerAdapter {
    @Deprecated(forRemoval = true, since = "2.0.1")
    public static Cache cache() {
        return new Cache();
    }

    @Deprecated(forRemoval = true, since = "2.0.1")
    public static Data data() {
        return new Data();
    }

    @Deprecated(forRemoval = true, since = "2.0.1")
    public static UUID uuid() {
        return new UUID();
    }

    /**
     * This adapter requires no active internet connection and accesses the server's own player cache.<br>
     * The downsides of this adapter are that player names are used rather than uuids,<br>
     * also players may not be found even though they already have a profile on the server.
     *
     * @see Bukkit#getOfflinePlayerIfCached(String)
     * @see Data
     * @see UUID
     */
    public static final class Cache implements PaperAdapter<OfflinePlayer> {
        @Override
        public @Nullable OfflinePlayer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return element.isJsonPrimitive() ? Bukkit.getOfflinePlayerIfCached(element.getAsString()) : null;
        }

        @Override
        public JsonElement serialize(@Nullable OfflinePlayer source, Type type, JsonSerializationContext context) {
            return source != null && source.getName() != null ? new JsonPrimitive(source.getName()) : JsonNull.INSTANCE;
        }
    }

    /**
     * This adapter requires no active internet connection and accesses the server's own player data.<br>
     * The only downside to this is that it may be expensive the more players ever connected to the server.
     *
     * @see Bukkit#getOfflinePlayers()
     * @see Cache
     * @see UUID
     */
    public static final class Data implements PaperAdapter<OfflinePlayer> {
        @Override
        public @Nullable OfflinePlayer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (!element.isJsonPrimitive()) return null;
            var uuid = java.util.UUID.fromString(element.getAsString());
            return Arrays.stream(Bukkit.getOfflinePlayers())
                    .filter(player -> player.getUniqueId().equals(uuid))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public JsonElement serialize(@Nullable OfflinePlayer source, Type type, JsonSerializationContext context) {
            return source != null ? new JsonPrimitive(source.getUniqueId().toString()) : JsonNull.INSTANCE;
        }
    }

    /**
     * This adapter uses the uuid to de/serialize offline players.<br>
     * It may be expensive to use this adapter because it requires an active internet connection.
     *
     * @see Bukkit#getOfflinePlayer(java.util.UUID)
     * @see Cache
     * @see Data
     */
    public static final class UUID implements PaperAdapter<OfflinePlayer> {
        @Override
        public @Nullable OfflinePlayer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (!element.isJsonPrimitive()) return null;
            return Bukkit.getOfflinePlayer(java.util.UUID.fromString(element.getAsString()));
        }

        @Override
        public JsonElement serialize(@Nullable OfflinePlayer source, Type type, JsonSerializationContext context) {
            return source != null ? new JsonPrimitive(source.getUniqueId().toString()) : JsonNull.INSTANCE;
        }
    }
}

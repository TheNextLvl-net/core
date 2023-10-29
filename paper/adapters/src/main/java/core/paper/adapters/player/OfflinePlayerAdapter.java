package core.paper.adapters.player;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Arrays;

public abstract class OfflinePlayerAdapter extends PaperAdapter<OfflinePlayer> {

    /**
     * @see Bukkit#getOfflinePlayer(java.util.UUID)
     * @see Cache
     * @see Data
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UUID extends OfflinePlayerAdapter {
        public static final OfflinePlayerAdapter INSTANCE = new UUID();

        @Override
        public OfflinePlayer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return Bukkit.getOfflinePlayer(java.util.UUID.fromString(element.getAsString()));
        }

        @Override
        public JsonElement serialize(OfflinePlayer source, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(source.getUniqueId().toString());
        }
    }

    /**
     * This adapter is obsolete and should not be used for security reasons.
     *
     * @see UUID
     */
    @ApiStatus.Obsolete()
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Name extends OfflinePlayerAdapter {
        public static final OfflinePlayerAdapter INSTANCE = new Name();

        @Override
        public @Nullable OfflinePlayer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return element.isJsonPrimitive() ? Bukkit.getOfflinePlayer(element.getAsString()) : null;
        }

        @Override
        public JsonElement serialize(OfflinePlayer source, Type type, JsonSerializationContext context) {
            return source.getName() != null ? new JsonPrimitive(source.getName()) : JsonNull.INSTANCE;
        }
    }

    /**
     * This adapter requires no active internet connection and accesses the servers own player cache.<br>
     * The downsides of this adapter are that player names are used rather than uuids,<br>
     * also players may not be found even though they already have a profile on the server.
     *
     * @see Bukkit#getOfflinePlayerIfCached(String)
     * @see Data
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Cache extends OfflinePlayerAdapter {
        public static final OfflinePlayerAdapter INSTANCE = new Cache();

        @Override
        public @Nullable OfflinePlayer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return element.isJsonPrimitive() ? Bukkit.getOfflinePlayerIfCached(element.getAsString()) : null;
        }

        @Override
        public JsonElement serialize(OfflinePlayer source, Type type, JsonSerializationContext context) {
            return source.getName() != null ? new JsonPrimitive(source.getName()) : JsonNull.INSTANCE;
        }
    }

    /**
     * This adapter requires no active internet connection and accesses the servers own player data.<br>
     * The only downside to this is that it may be expensive the more players ever connected to the server.
     *
     * @see Bukkit#getOfflinePlayers()
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Data extends OfflinePlayerAdapter {
        public static final OfflinePlayerAdapter INSTANCE = new Cache();

        @Override
        public @Nullable OfflinePlayer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            var uuid = java.util.UUID.fromString(element.getAsString());
            return Arrays.stream(Bukkit.getOfflinePlayers())
                    .filter(player -> player.getUniqueId().equals(uuid))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public JsonElement serialize(OfflinePlayer source, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(source.getUniqueId().toString());
        }
    }
}

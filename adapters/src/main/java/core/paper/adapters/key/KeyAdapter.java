package core.paper.adapters.key;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * This adapter provides various adapters for key de/serialization
 */
@NullMarked
public final class KeyAdapter {
    public static Kyori kyori() {
        return new Kyori();
    }

    public static Bukkit bukkit() {
        return new Bukkit();
    }

    /**
     * This adapter is for de/serialization of keys backed by kyori
     *
     * @see Key
     */
    @SuppressWarnings("PatternValidation")
    public static final class Kyori implements PaperAdapter<Key> {
        @Override
        public @Nullable Key deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return element.isJsonPrimitive() ? Key.key(element.getAsString()) : null;
        }

        @Override
        public JsonElement serialize(@Nullable Key source, Type type, JsonSerializationContext context) {
            return source != null ? new JsonPrimitive(source.toString()) : JsonNull.INSTANCE;
        }
    }

    /**
     * This adapter is for de/serialization of keys backed by bukkit
     *
     * @see NamespacedKey
     */
    public static final class Bukkit implements PaperAdapter<NamespacedKey> {
        @Override
        public @Nullable NamespacedKey deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (!element.isJsonPrimitive()) return null;
            var split = element.getAsString().split(":", 2);
            return new NamespacedKey(split[0], split[1]);
        }

        @Override
        public JsonElement serialize(@Nullable NamespacedKey source, Type type, JsonSerializationContext context) {
            return source != null ? new JsonPrimitive(source.toString()) : JsonNull.INSTANCE;
        }
    }
}

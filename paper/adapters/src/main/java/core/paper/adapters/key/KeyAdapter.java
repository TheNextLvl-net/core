package core.paper.adapters.key;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;

/**
 * This adapter provides various adapter for key de/serialization
 *
 * @param <K> the type of key to de/serialize
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class KeyAdapter<K extends Key> extends PaperAdapter<K> {

    /**
     * This adapter is for de/serialization of keys backed by kyorie
     * @see Key
     */
    @SuppressWarnings("PatternValidation")
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Kyori extends KeyAdapter<Key> {
        public static final KeyAdapter<Key> INSTANCE = new Kyori();

        @Override
        public Key deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return Key.key(element.getAsString());
        }

        @Override
        public JsonElement serialize(Key key, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(key.toString());
        }
    }

    /**
     * This adapter is for de/serialization of keys backed by bukkit
     * @see NamespacedKey
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Bukkit extends KeyAdapter<NamespacedKey> {
        public static final KeyAdapter<NamespacedKey> INSTANCE = new Bukkit();

        @Override
        public NamespacedKey deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            var split = element.getAsString().split(":", 2);
            return new NamespacedKey(split[0], split[1]);
        }

        @Override
        public JsonElement serialize(NamespacedKey source, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(source.toString());
        }
    }
}

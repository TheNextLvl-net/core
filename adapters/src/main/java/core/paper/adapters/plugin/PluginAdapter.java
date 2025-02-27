package core.paper.adapters.plugin;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * This adapter de/serializes {@link Plugin plugins} by their name
 *
 * @see PluginManager#getPlugin(String)
 */
@NullMarked
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PluginAdapter implements PaperAdapter<Plugin> {
    public static PluginAdapter instance() {
        return new PluginAdapter();
    }

    @Override
    public @Nullable Plugin deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return element.isJsonPrimitive() ? Bukkit.getPluginManager().getPlugin(element.getAsString()) : null;
    }

    @Override
    public JsonElement serialize(@Nullable Plugin source, Type type, JsonSerializationContext context) {
        return source != null ? new JsonPrimitive(source.getName()) : JsonNull.INSTANCE;
    }
}

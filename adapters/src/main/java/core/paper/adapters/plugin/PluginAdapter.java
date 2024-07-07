package core.paper.adapters.plugin;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * This adapter de/serializes {@link Plugin plugins} by their name
 *
 * @see PluginManager#getPlugin(String)
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PluginAdapter extends PaperAdapter<Plugin> {
    public static final PluginAdapter INSTANCE = new PluginAdapter();

    @Override
    public @Nullable Plugin deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return Bukkit.getPluginManager().getPlugin(element.getAsString());
    }

    @Override
    public JsonElement serialize(Plugin source, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(source.getName());
    }
}

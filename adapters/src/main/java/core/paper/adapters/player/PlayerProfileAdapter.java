package core.paper.adapters.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * This adapter de/serializes player profiles
 */
@NullMarked
public final class PlayerProfileAdapter implements PaperAdapter<PlayerProfile> {
    public static PlayerProfileAdapter instance() {
        return new PlayerProfileAdapter();
    }

    @Override
    public @Nullable PlayerProfile deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!element.isJsonObject()) return null;
        var object = element.getAsJsonObject();
        var name = object.has("name") ? object.get("name").getAsString() : null;
        var uuid = object.has("uuid") ? context.<UUID>deserialize(object.get("uuid"), UUID.class) : null;
        var properties = object.getAsJsonArray("properties").asList().stream()
                .map(property -> context.<ProfileProperty>deserialize(property, ProfileProperty.class))
                .toList();
        var profile = Bukkit.createProfile(uuid, name);
        profile.setProperties(properties);
        return profile;
    }

    @Override
    public JsonElement serialize(@Nullable PlayerProfile profile, Type type, JsonSerializationContext context) {
        if (profile == null) return JsonNull.INSTANCE;
        if (profile.getName() == null && profile.getId() == null) return JsonNull.INSTANCE;
        var object = new JsonObject();
        if (profile.getName() != null) object.addProperty("name", profile.getName());
        if (profile.getId() != null) object.add("uuid", context.serialize(profile.getId()));
        object.add("properties", context.serialize(profile.getProperties()));
        return object;
    }
}

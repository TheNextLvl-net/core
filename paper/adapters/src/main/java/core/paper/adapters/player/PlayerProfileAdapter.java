package core.paper.adapters.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import org.bukkit.Bukkit;

import java.lang.reflect.Type;
import java.util.UUID;

public class PlayerProfileAdapter extends PaperAdapter<PlayerProfile> {
    public static final PlayerProfileAdapter INSTANCE = new PlayerProfileAdapter();

    @Override
    public PlayerProfile deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        var object = element.getAsJsonObject();
        var name = object.has("name") ? object.get("name").getAsString() : null;
        var uuid = object.has("uuid") ? UUID.fromString(object.get("uuid").getAsString()) : null;
        var properties = object.getAsJsonArray("properties").asList().stream()
                .map(property -> context.<ProfileProperty>deserialize(property, ProfileProperty.class))
                .toList();
        var profile = Bukkit.createProfile(uuid, name);
        profile.setProperties(properties);
        return profile;
    }

    @Override
    public JsonElement serialize(PlayerProfile profile, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        if (profile.getName() != null) object.addProperty("name", profile.getName());
        if (profile.getId() != null) object.addProperty("uuid", profile.getId().toString());
        object.add("properties", context.serialize(profile.getProperties()));
        return object;
    }
}

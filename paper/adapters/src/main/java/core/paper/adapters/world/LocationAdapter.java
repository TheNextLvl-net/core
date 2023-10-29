package core.paper.adapters.world;

import com.google.gson.*;
import core.paper.adapters.api.PaperAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

/**
 * This adapter requires a {@link WorldAdapter} to properly function
 */
public abstract class LocationAdapter extends PaperAdapter<Location> {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Complex extends LocationAdapter {
        public static final LocationAdapter INSTANCE = new Complex();

        @Override
        public Location deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            var object = element.getAsJsonObject();
            var world = context.<World>deserialize(object.getAsJsonPrimitive("world"), World.class);
            var x = object.get("x").getAsDouble();
            var y = object.get("y").getAsDouble();
            var z = object.get("z").getAsDouble();
            var yaw = object.has("yaw") ? object.get("yaw").getAsFloat() : 0;
            var pitch = object.has("pitch") ? object.get("pitch").getAsFloat() : 0;
            return new Location(world, x, y, z, yaw, pitch);
        }

        @Override
        public JsonElement serialize(Location source, Type type, JsonSerializationContext context) {
            var object = new JsonObject();
            object.add("world", context.serialize(source.getWorld()));
            object.addProperty("x", source.getX());
            object.addProperty("y", source.getY());
            object.addProperty("z", source.getZ());
            if (source.getYaw() != 0 || source.getPitch() != 0) return object;
            object.addProperty("yaw", source.getYaw());
            object.addProperty("pitch", source.getPitch());
            return object;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Simple extends LocationAdapter {
        public static final LocationAdapter INSTANCE = new Simple();

        @Override
        public Location deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            var split = element.getAsString().split(", ");
            var world = context.<World>deserialize(new JsonPrimitive(split[0]), World.class);
            var x = Double.parseDouble(split[1]);
            var y = Double.parseDouble(split[2]);
            var z = Double.parseDouble(split[3]);
            var yaw = split.length == 6 ? Float.parseFloat(split[4]) : 0;
            var pitch = split.length == 6 ? Float.parseFloat(split[5]) : 0;
            return new Location(world, x, y, z, yaw, pitch);
        }

        @Override
        public JsonElement serialize(Location source, Type type, JsonSerializationContext context) {
            var builder = new StringBuilder(context.serialize(source.getWorld()).getAsString())
                    .append(", ").append(source.getX())
                    .append(", ").append(source.getY())
                    .append(", ").append(source.getZ());
            if (source.getYaw() != 0 || source.getPitch() != 0)
                builder.append(", ").append(source.getYaw())
                        .append(", ").append(source.getPitch());
            return new JsonPrimitive(builder.toString());
        }
    }
}
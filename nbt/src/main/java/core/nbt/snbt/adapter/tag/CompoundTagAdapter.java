package core.nbt.snbt.adapter.tag;

import com.google.gson.*;
import core.nbt.tag.CompoundTag;
import core.nbt.tag.Tag;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;

@ApiStatus.Internal
public class CompoundTagAdapter implements JsonSerializer<CompoundTag>, JsonDeserializer<CompoundTag> {
    @Override
    public CompoundTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        var compoundTag = new CompoundTag();
        var object = element.getAsJsonObject();
        object.entrySet().forEach(entry -> compoundTag.add(entry.getKey(),
                context.<Tag>deserialize(entry.getValue(), Tag.class)));
        return compoundTag;
    }

    @Override
    public JsonObject serialize(CompoundTag compoundTag, Type type, JsonSerializationContext context) {
        var object = new JsonObject();
        compoundTag.forEach((name, tag) -> object.add(name, context.serialize(tag)));
        return object;
    }
}

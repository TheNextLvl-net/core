package core.nbt.adapter;

import com.google.gson.*;
import core.nbt.tag.EscapeTag;

import java.lang.reflect.Type;

public class EscapeTagAdapter implements JsonSerializer<EscapeTag>, JsonDeserializer<EscapeTag> {
    @Override
    public EscapeTag deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return EscapeTag.INSTANCE;
    }

    @Override
    public JsonNull serialize(EscapeTag tag, Type type, JsonSerializationContext context) {
        return JsonNull.INSTANCE;
    }
}

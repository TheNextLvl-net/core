package core.paper.adapters.api;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This class is just a combination of the {@link JsonSerializer} and {@link JsonDeserializer}.<br>
 *
 * @param <T> the type for which this adapter is registered
 */
public abstract class PaperAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
    @Override
    public abstract T deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException;

    @Override
    public abstract JsonElement serialize(T source, Type type, JsonSerializationContext context);
}

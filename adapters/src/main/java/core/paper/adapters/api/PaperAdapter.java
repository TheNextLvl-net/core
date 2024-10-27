package core.paper.adapters.api;

import com.google.gson.*;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * This class is a combination of the {@link JsonSerializer} and {@link JsonDeserializer}.
 *
 * @param <T> the type for which this adapter is registered
 */
@NullMarked
public interface PaperAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {
    @Override
    @Nullable
    T deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException;

    @Override
    JsonElement serialize(@Nullable T source, Type type, JsonSerializationContext context);
}

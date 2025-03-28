package core.paper.adapters.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * This class is a combination of the {@link JsonSerializer} and {@link JsonDeserializer}.
 *
 * @param <T> the type for which this adapter is registered
 */
public interface PaperAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {
    @Nullable
    @Override
    T deserialize(@NonNull JsonElement element, @NonNull Type type, @NonNull JsonDeserializationContext context) throws JsonParseException;

    @NonNull
    @Override
    JsonElement serialize(@Nullable T source, @NonNull Type type, @NonNull JsonSerializationContext context);
}

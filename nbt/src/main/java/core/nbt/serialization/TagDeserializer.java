package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Interface for deserializing a Tag into a specific type.
 *
 * @param <T> the type into which the Tag should be deserialized
 */
public interface TagDeserializer<T> {
    /**
     * Deserializes the given Tag into the specified type.
     *
     * @param tag     the tag to be deserialized
     * @param context the context used for deserialization
     * @return the deserialized object of type T
     */
    @Nullable
    T deserialize(@NonNull Tag tag, @NonNull TagDeserializationContext context);
}

package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NonNull;

/**
 * Interface for serializing objects of type {@code T} into {@link Tag} representations.
 *
 * @param <T> the type of the objects to be serialized
 */
public interface TagSerializer<T> {
    /**
     * Serializes a given object into its corresponding Tag representation.
     *
     * @param object  the object to be serialized
     * @param context the context used for serialization
     * @return the Tag representation of the provided vector
     * @throws ParserException if an error occurs during serialization
     */
    @NonNull
    Tag serialize(@NonNull T object, @NonNull TagSerializationContext context) throws ParserException;
}

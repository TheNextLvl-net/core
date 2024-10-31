package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

/**
 * Interface for serializing objects of type {@code T} into {@link Tag} representations.
 *
 * @param <T> the type of the objects to be serialized
 */
@NullMarked
public interface TagSerializer<T> {
    /**
     * Serializes a given vector into its corresponding Tag representation.
     *
     * @param vector the vector to be serialized
     * @return the Tag representation of the provided vector
     */
    Tag serialize(T vector);
}

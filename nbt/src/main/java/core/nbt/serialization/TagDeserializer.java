package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

/**
 * Interface for deserializing a Tag into a specific type.
 *
 * @param <T> the type into which the Tag should be deserialized
 */
@NullMarked
public interface TagDeserializer<T> {
    /**
     * Deserializes the given Tag into the specified type.
     *
     * @param tag the tag to be deserialized
     * @return the deserialized object of type T
     */
    T deserialize(Tag tag);
}
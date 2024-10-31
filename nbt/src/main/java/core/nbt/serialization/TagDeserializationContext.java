package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

/**
 * Interface defining the context for deserializing tags into their respective objects.
 */
@NullMarked
public interface TagDeserializationContext {
    /**
     * Deserializes a given tag to an object of the specified type.
     *
     * @param tag the tag to be deserialized
     * @param type the class of the object to be returned
     * @param <T> the type of the object to be returned
     * @return an object of the specified type deserialized from the tag
     */
    <T> T deserialize(Tag tag, Class<T> type);
}

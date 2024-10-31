package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * TagSerializationContext defines the contract for serializing objects into tags.
 */
public interface TagSerializationContext {
    /**
     * Serializes the given object into a Tag representation.
     *
     * @param object the object to be serialized
     * @return the serialized Tag representation of the object
     */
    @NonNull
    Tag serialize(@NonNull Object object);

    /**
     * Serializes the given object into a Tag representation based on the specified type.
     *
     * @param object the object to be serialized
     * @param type   the class type of the object to be serialized
     * @return the serialized Tag representation of the object
     */
    @NonNull
    Tag serialize(@Nullable Object object, @NonNull Class<?> type);
}

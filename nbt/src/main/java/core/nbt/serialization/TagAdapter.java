package core.nbt.serialization;

import org.jspecify.annotations.NullMarked;

/**
 * Combines functionality of {@link TagDeserializer} and {@link TagSerializer}
 * for handling both serialization and deserialization of the specified type.
 *
 * @param <T> the type of objects handled by this adapter
 */
@NullMarked
public interface TagAdapter<T> extends TagDeserializer<T>, TagSerializer<T> {
}

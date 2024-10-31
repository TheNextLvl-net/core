package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;
import java.text.ParseException;

/**
 * TagSerializationContext defines the contract for serializing objects into tags.
 */
public interface TagSerializationContext {
    /**
     * Serializes the given object into a Tag representation.
     *
     * @param object the object to be serialized
     * @return the serialized Tag representation of the object
     * @throws ParserException if an error occurs during serialization
     */
    @NonNull
    Tag serialize(@Nullable Object object) throws ParseException;

    /**
     * Serializes the given object into a Tag representation based on the specified type.
     *
     * @param object the object to be serialized
     * @param type   the type of the object to be serialized
     * @return the serialized Tag representation of the object
     * @throws ParserException if an error occurs during serialization
     */
    @NonNull
    Tag serialize(@Nullable Object object, @NonNull Type type) throws ParseException;
}

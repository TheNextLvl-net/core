package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;

/**
 * TagSerializationContext defines the contract for serializing objects into tags.
 */
@NullMarked
public interface TagSerializationContext {
    /**
     * Serializes the given object into a Tag representation.
     *
     * @param object the object to be serialized
     * @return the serialized Tag representation of the object
     * @throws ParserException if an error occurs during serialization
     */
    @Contract(value = "_ -> new", pure = true)
    Tag serialize(Object object) throws ParserException;

    /**
     * Serializes the given object into a Tag representation based on the specified type.
     *
     * @param object the object to be serialized
     * @param type   the class type of the object to be serialized
     * @return the serialized Tag representation of the object
     * @throws ParserException if an error occurs during serialization
     */
    @Contract(value = "_, _ -> new", pure = true)
    Tag serialize(Object object, Class<?> type) throws ParserException;

    /**
     * Serializes the given object into a Tag representation based on the specified type.
     *
     * @param object the object to be serialized
     * @param type   the type of the object to be serialized
     * @return the serialized Tag representation of the object
     * @throws ParserException if an error occurs during serialization
     */
    @Contract(value = "_, _ -> new", pure = true)
    Tag serialize(Object object, Type type) throws ParserException;
}

package core.nbt.serialization;

import core.nbt.tag.Tag;

/**
 * An interface for objects that can be serialized into and deserialized from a Tag representation.
 * Implementing classes should provide specific mechanisms for converting their data to and from Tag instances.
 */
public interface TagSerializable {
    /**
     * Serializes the current object into a Tag representation.
     *
     * @return the serialized Tag representation of the current object
     */
    Tag serialize();

    /**
     * Deserializes the given Tag into the appropriate object.
     *
     * @param tag the Tag object to be deserialized
     */
    void deserialize(Tag tag);
}

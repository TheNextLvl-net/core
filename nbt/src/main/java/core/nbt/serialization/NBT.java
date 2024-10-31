package core.nbt.serialization;

import core.nbt.tag.Tag;

/**
 * The NBT class provides methods to serialize and deserialize objects to and from NBT tags,
 * as well as to register custom serializers and deserializers for different types.
 */
public final class NBT {
    private final Serializer serializer;

    /**
     * Constructs an instance of NBT using the provided Serializer.
     *
     * @param serializer the serializer to be used for serialization and deserialization operations
     */
    private NBT(Serializer serializer) {
        this.serializer = serializer;
    }

    /**
     * Creates a new instance of the Builder class for constructing NBT objects.
     *
     * @return a new Builder instance for constructing NBT objects
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Deserializes a given {@link Tag} into the specified type.
     *
     * @param tag  the tag to be deserialized
     * @param type the class of the type to deserialize into
     * @return the deserialized object of the specified type
     */
    public <T> T fromTag(Tag tag, Class<T> type) {
        return serializer.deserialize(tag, type);
    }

    /**
     * Serializes the given object into its corresponding Tag representation.
     *
     * @param object the object to be serialized
     * @return the Tag representation of the provided object
     */
    public Tag toTag(Object object) {
        return serializer.serialize(object);
    }

    /**
     * Serializes the given object to a Tag representation for the specified type.
     *
     * @param object the object to be serialized
     * @param type   the class type to be used for serialization
     * @return the serialized tag representation of the object
     */
    public Tag toTag(Object object, Class<?> type) {
        return serializer.serialize(object, type);
    }

    public static class Builder {
        private final Serializer serializer = new Serializer();

        /**
         * Registers a custom deserializer for the specified type.
         *
         * @param type         the class of the type for which the deserializer is to be registered
         * @param deserializer the instance of TagDeserializer to handle deserializing the specified type
         * @return the current builder instance for chaining
         */
        public <T> Builder registerDeserializer(Class<T> type, TagDeserializer<T> deserializer) {
            this.serializer.registerDeserializer(type, deserializer);
            return this;
        }

        /**
         * Registers a custom serializer for the specified type.
         *
         * @param <T>        the type of the objects to be serialized
         * @param type       the class of the type for which the serializer is to be registered
         * @param serializer the instance of TagSerializer to handle serializing the specified type
         * @return the current builder instance for chaining
         */
        public <T> Builder registerSerializer(Class<T> type, TagSerializer<T> serializer) {
            this.serializer.registerSerializer(type, serializer);
            return this;
        }

        /**
         * Constructs and returns an instance of NBT using the configured serializers and deserializers.
         *
         * @return a new instance of NBT
         */
        public NBT build() {
            return new NBT(serializer);
        }
    }
}

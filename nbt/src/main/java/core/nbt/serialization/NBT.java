package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;

/**
 * The NBT class provides methods to serialize and deserialize objects to and from NBT tags,
 * as well as to register custom serializers and deserializers for different types.
 */
@NullMarked
public final class NBT extends Serializer {
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
     * @param <T>  the type of the object to be returned
     * @return the deserialized object of the specified type
     * @deprecated use {@link #deserialize(Tag, Class)} instead
     */
    @Deprecated(forRemoval = true, since = "2.4.0")
    default <T> T fromTag(Tag tag, Class<T> type) {
        return deserialize(tag, type);
    }

    /**
     * Deserializes a given {@link Tag} into the specified type.
     *
     * @param tag  the tag to be deserialized
     * @param type the type to deserialize into
     * @param <T>  the type of the object to be returned
     * @return the deserialized object of the specified type
     * @deprecated use {@link #deserialize(Tag, Type)} instead
     */
    @Deprecated(forRemoval = true, since = "2.4.0")
    default <T> T fromTag(Tag tag, Type type) {
        return deserialize(tag, type);
    }

    /**
     * Serializes the given object into its corresponding Tag representation.
     *
     * @param object the object to be serialized
     * @return the Tag representation of the provided object
     * @deprecated use {@link #serialize(Object)} instead
     */
    @Deprecated(forRemoval = true, since = "2.4.0")
    default Tag toTag(Object object) {
        return serialize(object);
    }

    /**
     * Serializes the given object to a Tag representation for the specified type.
     *
     * @param object the object to be serialized
     * @param type   the class type to be used for serialization
     * @return the serialized tag representation of the object
     * @deprecated use {@link #serialize(Object, Class)} instead
     */
    @Deprecated(forRemoval = true, since = "2.4.0")
    default Tag toTag(Object object, Class<?> type) {
        return serialize(object, type);
    }

    /**
     * Serializes the given object to a Tag representation for the specified type.
     *
     * @param object the object to be serialized
     * @param type   the type to be used for serialization
     * @return the serialized tag representation of the object
     * @deprecated use {@link #serialize(Object, Type)} instead
     */
    @Deprecated(forRemoval = true, since = "2.4.0")
    default Tag toTag(Object object, Type type) {
        return serialize(object, type);
    }

    /**
     * Builder class for constructing instances of NBT with custom serializers and deserializers.
     */
    public static class Builder {
        private final Serializer serializer = new Serializer();

        /**
         * Registers a custom adapter for both serialization and deserialization of the specified type
         * and its subtypes.
         *
         * @param <T>     the type of the objects handled by the adapter
         * @param type    the class of the type for which the adapter is to be registered
         * @param adapter the instance of TagAdapter to handle both serialization and deserialization
         *                of the specified type and its subtypes
         * @return the current builder instance for chaining
         */
        public <T> Builder registerTypeHierarchyAdapter(Class<?> type, TagAdapter<T> adapter) {
            this.serializer.registerTypeHierarchyAdapter(type, adapter);
            return this;
        }

        /**
         * Registers a custom deserializer for the specified type or any of its subtypes.
         *
         * @param <T>          the type of the objects handled by the deserializer
         * @param type         the class of the type for which the deserializer is to be registered
         * @param deserializer the instance of TagDeserializer to handle deserializing the specified type
         * @return the current builder instance for chaining
         */
        public <T> Builder registerTypeHierarchyAdapter(Class<?> type, TagDeserializer<T> deserializer) {
            this.serializer.registerTypeHierarchyAdapter(type, deserializer);
            return this;
        }

        /**
         * Registers a custom serializer for the specified type or any of its subtypes.
         *
         * @param <T>        the type of the objects to be serialized
         * @param type       the class of the type for which the serializer is to be registered
         * @param serializer the instance of TagSerializer to handle serializing the specified type
         * @return the current builder instance for chaining
         */
        public <T> Builder registerTypeHierarchyAdapter(Class<?> type, TagSerializer<T> serializer) {
            this.serializer.registerTypeHierarchyAdapter(type, serializer);
            return this;
        }

        /**
         * Registers a custom adapter for both serialization and deserialization of the specified type.
         *
         * @param <T>     the type of the objects handled by the adapter
         * @param type    the class of the type for which the adapter is to be registered
         * @param adapter the instance of TagAdapter to handle both serialization and deserialization of the specified type
         * @return the current builder instance for chaining
         */
        public <T> Builder registerTypeAdapter(Type type, TagAdapter<T> adapter) {
            this.serializer.registerTypeAdapter(type, adapter);
            return this;
        }

        /**
         * Registers a custom deserializer for the specified type.
         *
         * @param <T>          the type of the objects handled by the deserializer
         * @param type         the class of the type for which the deserializer is to be registered
         * @param deserializer the instance of TagDeserializer to handle deserializing the specified type
         * @return the current builder instance for chaining
         */
        public <T> Builder registerTypeAdapter(Type type, TagDeserializer<T> deserializer) {
            this.serializer.registerTypeAdapter(type, deserializer);
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
        public <T> Builder registerTypeAdapter(Type type, TagSerializer<T> serializer) {
            this.serializer.registerTypeAdapter(type, serializer);
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

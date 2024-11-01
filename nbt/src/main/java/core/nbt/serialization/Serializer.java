package core.nbt.serialization;

import core.nbt.serialization.adapter.*;
import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NullMarked
class Serializer implements TagDeserializationContext, TagSerializationContext {
    private final Map<Class<?>, TagDeserializer<?>> hierarchyDeserializers = new HashMap<>();
    private final Map<Class<?>, TagSerializer<?>> hierarchySerializers = new HashMap<>();

    private final Map<Type, TagDeserializer<?>> deserializers = new HashMap<>();
    private final Map<Type, TagSerializer<?>> serializers = new HashMap<>();

    public Serializer() {
        registerTypeAdapter(Boolean.class, new BooleanAdapter());
        registerTypeAdapter(Byte.class, new ByteAdapter());
        registerTypeAdapter(Double.class, new DoubleAdapter());
        registerTypeAdapter(Float.class, new FloatAdapter());
        registerTypeAdapter(Integer.class, new IntegerAdapter());
        registerTypeAdapter(Long.class, new LongAdapter());
        registerTypeAdapter(Short.class, new ShortAdapter());
        registerTypeAdapter(String.class, new StringAdapter());
        registerTypeAdapter(UUID.class, new UUIDAdapter());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(Tag tag, Class<T> type) throws ParserException {
        var deserializer = hierarchyDeserializers.get(type);
        if (deserializer != null) return (T) deserializer.deserialize(tag, this);
        return hierarchyDeserializers.entrySet().stream()
                .filter(entry -> type.isAssignableFrom(entry.getKey()))
                .findAny()
                .map(entry -> (T) entry.getValue().deserialize(tag, this))
                .orElseGet(() -> deserialize(tag, (Type) type));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(Tag tag, Type type) throws ParserException {
        var deserializer = deserializers.get(type);
        if (deserializer != null) return (T) deserializer.deserialize(tag, this);
        throw new ParserException("No tag deserializer registered for type: " + type);
    }

    @Override
    public Tag serialize(Object object) throws ParserException {
        return serialize(object, object.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tag serialize(Object object, Class<?> type) throws ParserException {
        var serializer = (TagSerializer<Object>) hierarchySerializers.get(object.getClass());
        if (serializer != null) return serializer.serialize(object, this);
        return hierarchySerializers.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(object))
                .findAny()
                .map(entry -> (TagSerializer<Object>) entry.getValue())
                .map(value -> value.serialize(object, this))
                .orElseGet(() -> serialize(object, (Type) object.getClass()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tag serialize(Object object, Type type) throws ParserException {
        var serializer = (TagSerializer<Object>) serializers.get(type);
        if (serializer != null) return serializer.serialize(object, this);
        throw new ParserException("No tag serializer registered for type: " + type);
    }

    public void registerTypeHierarchyAdapter(Class<?> clazz, TagAdapter<?> adapter) {
        hierarchyDeserializers.put(clazz, adapter);
        hierarchySerializers.put(clazz, adapter);
    }

    public void registerTypeHierarchyAdapter(Class<?> clazz, TagDeserializer<?> deserializer) {
        hierarchyDeserializers.put(clazz, deserializer);
    }

    public void registerTypeHierarchyAdapter(Class<?> clazz, TagSerializer<?> serializer) {
        hierarchySerializers.put(clazz, serializer);
    }
    
    public void registerTypeAdapter(Type type, TagAdapter<?> adapter) {
        deserializers.put(type, adapter);
        serializers.put(type, adapter);
    }

    public void registerTypeAdapter(Type type, TagDeserializer<?> deserializer) {
        deserializers.put(type, deserializer);
    }

    public void registerTypeAdapter(Type type, TagSerializer<?> serializer) {
        serializers.put(type, serializer);
    }
}

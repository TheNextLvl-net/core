package core.nbt.serialization;

import core.nbt.serialization.adapter.BooleanAdapter;
import core.nbt.serialization.adapter.ByteAdapter;
import core.nbt.serialization.adapter.DoubleAdapter;
import core.nbt.serialization.adapter.FileAdapter;
import core.nbt.serialization.adapter.FloatAdapter;
import core.nbt.serialization.adapter.IntegerAdapter;
import core.nbt.serialization.adapter.LongAdapter;
import core.nbt.serialization.adapter.ShortAdapter;
import core.nbt.serialization.adapter.StringAdapter;
import core.nbt.serialization.adapter.UUIDAdapter;
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
        registerTypeAdapter(Boolean.class, BooleanAdapter.INSTANCE);
        registerTypeAdapter(Byte.class, ByteAdapter.INSTANCE);
        registerTypeAdapter(Double.class, DoubleAdapter.INSTANCE);
        registerTypeAdapter(Float.class, FloatAdapter.INSTANCE);
        registerTypeAdapter(Integer.class, IntegerAdapter.INSTANCE);
        registerTypeAdapter(Long.class, LongAdapter.INSTANCE);
        registerTypeAdapter(Short.class, ShortAdapter.INSTANCE);
        registerTypeAdapter(String.class, StringAdapter.INSTANCE);
        registerTypeAdapter(UUID.class, UUIDAdapter.INSTANCE);
        registerTypeAdapter(boolean.class, BooleanAdapter.INSTANCE);
        registerTypeAdapter(byte.class, ByteAdapter.INSTANCE);
        registerTypeAdapter(double.class, DoubleAdapter.INSTANCE);
        registerTypeAdapter(float.class, FloatAdapter.INSTANCE);
        registerTypeAdapter(int.class, IntegerAdapter.INSTANCE);
        registerTypeAdapter(long.class, LongAdapter.INSTANCE);
        registerTypeAdapter(short.class, ShortAdapter.INSTANCE);
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

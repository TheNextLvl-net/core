package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@NullMarked
class Serializer implements TagDeserializationContext, TagSerializationContext {
    private final Map<Type, TagDeserializer<?>> deserializers = new HashMap<>();
    private final Map<Type, TagSerializer<?>> serializers = new HashMap<>();

    @Override
    public <T> T deserialize(Tag tag, Class<T> type) throws ParserException {
        return deserialize(tag, (Type) type);
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
    public Tag serialize(Object object, Type type) throws ParserException {
        var serializer = (TagSerializer<Object>) serializers.get(type);
        if (serializer != null) return serializer.serialize(object, this);
        throw new ParserException("No tag serializer registered for type: " + type);
    }

    public void registerDeserializer(Type type, TagDeserializer<?> deserializer) {
        deserializers.put(type, deserializer);
    }

    public void registerSerializer(Type type, TagSerializer<?> serializer) {
        serializers.put(type, serializer);
    }
}

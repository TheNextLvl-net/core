package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

@NullMarked
class Serializer implements TagDeserializationContext, TagSerializationContext {
    private final Map<Class<?>, TagDeserializer<?>> deserializers = new HashMap<>();
    private final Map<Class<?>, TagSerializer<?>> serializers = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(Tag tag, Class<T> type) {
        return (T) deserializers.get(type).deserialize(tag, this);
    }

    @Override
    public Tag serialize(Object object) {
        return serialize(object, object.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tag serialize(Object object, Class<?> type) {
        var serializer = (TagSerializer<Object>) serializers.get(type);
        return serializer.serialize(object, this);
    }

    public void registerDeserializer(Class<?> type, TagDeserializer<?> deserializer) {
        deserializers.put(type, deserializer);
    }

    public void registerSerializer(Class<?> type, TagSerializer<?> serializer) {
        serializers.put(type, serializer);
    }
}

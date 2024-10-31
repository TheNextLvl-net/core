package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@NullMarked
class Serializer implements TagDeserializationContext, TagSerializationContext {
    private final Map<Class<?>, TagDeserializer<?>> deserializers = new HashMap<>();
    private final Map<Class<?>, TagSerializer<?>> serializers = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable <T> T deserialize(Tag tag, Class<T> type) {
        if (tag == Tag.EMPTY) return null;
        var deserializer = deserializers.get(type);
        if (deserializer == null) return null;
        return (T) deserializer.deserialize(tag, this);
    }

    @Override
    public Tag serialize(Object object) {
        return serialize(object, object.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tag serialize(@Nullable Object object, Class<?> type) {
        if (object == null) return Tag.EMPTY;
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

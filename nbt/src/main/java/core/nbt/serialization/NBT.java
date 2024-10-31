package core.nbt.serialization;

import core.nbt.tag.Tag;

public class NBT {
    private final Serializer serializer = new Serializer();

    public <T> T fromTag(Tag tag, Class<T> type) {
        return serializer.deserialize(tag, type);
    }

    public Tag toTag(Object object) {
        return serializer.serialize(object);
    }

    public Tag toTag(Object object, Class<?> type) {
        return serializer.serialize(object, type);
    }

    public <T> NBT registerDeserializer(Class<T> type, TagDeserializer<T> deserializer) {
        this.serializer.registerDeserializer(type, deserializer);
        return this;
    }

    public <T> NBT registerSerializer(Class<T> type, TagSerializer<T> serializer) {
        this.serializer.registerSerializer(type, serializer);
        return this;
    }
}

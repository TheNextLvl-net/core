package core.nbt.serialization;

public interface TagAdapter<T> extends TagDeserializer<T>, TagSerializer<T> {
}

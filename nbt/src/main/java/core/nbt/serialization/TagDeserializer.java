package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TagDeserializer<T> {
    T deserialize(Tag tag);
}

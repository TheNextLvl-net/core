package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TagSerializer<T> {
    Tag serialize(T vector);
}

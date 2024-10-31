package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TagSerializationContext {
    Tag serialize(Object object);

    Tag serialize(Object object, Class<?> type);
}

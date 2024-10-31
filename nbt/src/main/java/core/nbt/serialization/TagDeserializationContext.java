package core.nbt.serialization;

import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TagDeserializationContext {
    <T> T deserialize(Tag tag, Class<T> type);
}

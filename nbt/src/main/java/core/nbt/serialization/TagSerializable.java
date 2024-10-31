package core.nbt.serialization;

import core.nbt.tag.Tag;

public interface TagSerializable {
    Tag serialize();

    void deserialize(Tag tag);
}

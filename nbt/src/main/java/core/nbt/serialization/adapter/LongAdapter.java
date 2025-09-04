package core.nbt.serialization.adapter;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.LongTag;
import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class LongAdapter implements TagAdapter<Long> {
    public static final LongAdapter INSTANCE = new LongAdapter();

    @Override
    public Long deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return tag.getAsLong();
    }

    @Override
    public Tag serialize(Long object, TagSerializationContext context) throws ParserException {
        return new LongTag(object);
    }
}

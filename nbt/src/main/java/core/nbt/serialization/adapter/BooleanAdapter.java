package core.nbt.serialization.adapter;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.BooleanTag;
import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BooleanAdapter implements TagAdapter<Boolean> {
    public static final BooleanAdapter INSTANCE = new BooleanAdapter();

    @Override
    public Boolean deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return tag.getAsBoolean();
    }

    @Override
    public Tag serialize(Boolean object, TagSerializationContext context) throws ParserException {
        return new BooleanTag(object);
    }
}

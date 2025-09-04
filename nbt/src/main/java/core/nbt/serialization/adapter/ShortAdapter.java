package core.nbt.serialization.adapter;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.ShortTag;
import core.nbt.tag.Tag;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public final class ShortAdapter implements TagAdapter<Short> {
    public static final ShortAdapter INSTANCE = new ShortAdapter();

    @Override
    public Short deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return tag.getAsShort();
    }

    @Override
    public Tag serialize(Short object, TagSerializationContext context) throws ParserException {
        return new ShortTag(object);
    }
}

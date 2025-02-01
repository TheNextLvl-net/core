package core.nbt.serialization.adapter;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.ByteTag;
import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ByteAdapter implements TagAdapter<Byte> {
    public static final ByteAdapter INSTANCE = new ByteAdapter();

    @Override
    public Byte deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return tag.getAsByte();
    }

    @Override
    public Tag serialize(Byte object, TagSerializationContext context) throws ParserException {
        return new ByteTag(object);
    }
}

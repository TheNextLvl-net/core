package core.nbt.serialization.adapter;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.DoubleTag;
import core.nbt.tag.Tag;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public final class DoubleAdapter implements TagAdapter<Double> {
    public static final DoubleAdapter INSTANCE = new DoubleAdapter();

    @Override
    public Double deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return tag.getAsDouble();
    }

    @Override
    public Tag serialize(Double object, TagSerializationContext context) throws ParserException {
        return new DoubleTag(object);
    }
}

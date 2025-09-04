package core.nbt.serialization.adapter;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.IntTag;
import core.nbt.tag.Tag;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public final class IntegerAdapter implements TagAdapter<Integer> {
    public static final IntegerAdapter INSTANCE = new IntegerAdapter();

    private IntegerAdapter() {
    }
    
    @Override
    public Integer deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return tag.getAsInt();
    }

    @Override
    public Tag serialize(Integer object, TagSerializationContext context) throws ParserException {
        return new IntTag(object);
    }
}

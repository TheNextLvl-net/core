package core.nbt.serialization.adapter;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.StringTag;
import core.nbt.tag.Tag;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EnumAdapter<T extends Enum<T>> implements TagAdapter<T> {
    private final Class<T> enumClass;

    public EnumAdapter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public @NonNull T deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return Enum.valueOf(this.enumClass, tag.getAsString());
    }

    @Override
    public Tag serialize(T object, TagSerializationContext context) throws ParserException {
        return new StringTag(object.name());
    }
}

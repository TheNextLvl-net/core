package core.nbt.serialization.adapter;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.StringTag;
import core.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

import java.io.File;

@NullMarked
public final class FileAdapter implements TagAdapter<File> {
    public static final FileAdapter INSTANCE = new FileAdapter();

    @Override
    public File deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return new File(tag.getAsString());
    }

    @Override
    public Tag serialize(File file, TagSerializationContext context) throws ParserException {
        return new StringTag(file.getPath());
    }
}

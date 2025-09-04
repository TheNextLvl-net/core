package core.nbt.serialization.adapter;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.StringTag;
import core.nbt.tag.Tag;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.nio.file.Path;

@NullMarked
@ApiStatus.Internal
public final class PathAdapter implements TagAdapter<Path> {
    public static final PathAdapter INSTANCE = new PathAdapter();

    private PathAdapter() {
    }
    
    @Override
    public Path deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return Path.of(tag.getAsString());
    }

    @Override
    public Tag serialize(Path path, TagSerializationContext context) throws ParserException {
        return new StringTag(path.toString());
    }
}

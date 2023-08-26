package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class ListTag<V> extends ArrayList<Tag> implements Tag {
    public static final Type TYPE = new Type("ListTag", 9);
    private final @Nullable String name;
    private final @NotNull Type contentType;

    public ListTag(@Nullable String name, @NotNull Type contentType, Collection<? extends Tag> collection) {
        super(collection);
        this.name = name;
        this.contentType = contentType;
    }

    public ListTag(@Nullable String name, @NotNull Type contentType) {
        this.name = name;
        this.contentType = contentType;
    }

    @Override
    public @NotNull Type getType() {
        return TYPE;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeByte(getContentType().id());
        outputStream.writeInt(size());
        for (var tag : this) tag.write(outputStream);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append(getType().name())
                .append("(\"")
                .append(getName())
                .append("\"): ")
                .append(size())
                .append(" entries of type ")
                .append(getContentType().name())
                .append(" {\r\n");
        forEach(tag -> builder.append("   ")
                .append(tag.toString().replaceAll("\r\n", "\r\n   "))
                .append("\r\n"));
        builder.append("}");
        return builder.toString();
    }
}

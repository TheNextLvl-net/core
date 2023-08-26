package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CompoundTag extends HashMap<String, Tag> implements Tag {
    public static final Type TYPE = new Type("CompoundTag", 10);
    private final @Nullable String name;

    public CompoundTag(@Nullable String name, Map<? extends String, ? extends Tag> map) {
        super(map);
        this.name = name;
    }

    public CompoundTag(@Nullable String name) {
        this.name = name;
    }

    @Override
    public @NotNull Type getType() {
        return TYPE;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        for (var tag : values()) outputStream.writeTag(tag);
        EndTag.INSTANCE.write(outputStream);
    }

    @Override
    public String toString() {
        var name = new StringBuilder();
        if (getName() != null) name.append("(\"")
                .append(getName())
                .append("\")");
        var builder = new StringBuilder();
        builder.append(getType().name())
                .append(name)
                .append(": ")
                .append(size())
                .append(" entries {\r\n");
        forEach((key, value) -> builder.append("   ")
                .append(value.toString().replaceAll("\r\n", "\r\n   "))
                .append("\r\n"));
        builder.append("}");
        return builder.toString();
    }
}

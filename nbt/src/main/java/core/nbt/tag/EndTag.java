package core.nbt.tag;

import core.nbt.NBTOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class EndTag implements Tag {
    public static final Type TYPE = new Type("EndTag", 0);
    public static final EndTag INSTANCE = new EndTag();

    @Override
    public @Nullable String getName() {
        return null;
    }

    @Override
    public @NotNull Type getType() {
        return TYPE;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof EndTag;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeByte((byte) EndTag.TYPE.id());
    }

    @Override
    public String toString() {
        return getType().name();
    }
}

package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EscapeTag extends Tag {
    public static final int ID = 0;
    public static final EscapeTag INSTANCE = new EscapeTag();

    @Override
    public @Nullable String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof EscapeTag;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeByte((byte) getTypeId());
    }
}

package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EscapeTag implements Tag {
    public static final EscapeTag INSTANCE = new EscapeTag();
    public static final int ID = 0;

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj.getClass().equals(getClass());
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeByte((byte) getTypeId());
    }

    @Override
    public String toString() {
        return "EscapeTag.INSTANCE";
    }
}

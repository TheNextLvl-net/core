package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EscapeTag extends Tag {
    public static final int ID = 0;
    public static final EscapeTag INSTANCE = new EscapeTag();

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

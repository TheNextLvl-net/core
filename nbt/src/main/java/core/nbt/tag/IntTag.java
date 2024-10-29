package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public class IntTag extends NumberTag<Integer> {
    public static final int ID = 3;

    public IntTag(Integer value) {
        super(value);
    }

    @Override
    public int getAsInt() {
        return getValue();
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeInt(getValue());
    }

    public static IntTag read(NBTInputStream inputStream) throws IOException {
        return new IntTag(inputStream.readInt());
    }
}

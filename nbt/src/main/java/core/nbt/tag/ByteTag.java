package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;

import java.io.IOException;

public class ByteTag extends NumberTag<Byte> {
    public static final int ID = 1;

    public ByteTag(Byte value) {
        super(value);
    }

    @Override
    public byte getAsByte() {
        return getValue();
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.write(getValue());
    }

    public static ByteTag read(NBTInputStream inputStream) throws IOException {
        return new ByteTag(inputStream.readByte());
    }
}

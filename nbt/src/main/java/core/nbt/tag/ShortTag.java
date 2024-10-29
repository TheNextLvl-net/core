package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public class ShortTag extends NumberTag<Short> {
    public static final int ID = 2;

    public ShortTag(Short value) {
        super(value);
    }

    @Override
    public short getAsShort() {
        return getValue();
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeShort(getValue());
    }

    public static ShortTag read(NBTInputStream inputStream) throws IOException {
        return new ShortTag(inputStream.readShort());
    }
}

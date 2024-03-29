package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;

import java.io.IOException;

public class ByteArrayTag extends ValueTag<byte[]> implements IterableTag<Byte> {
    public static final int ID = 7;

    public ByteArrayTag(byte[] array) {
        super(array);
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public int size() {
        return getValue().length;
    }

    @Override
    public Byte get(int index) {
        return getValue()[index];
    }

    @Override
    public void set(int index, Byte element) {
        getValue()[index] = element;
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeInt(getValue().length);
        outputStream.write(getValue());
    }

    public static ByteArrayTag read(NBTInputStream inputStream) throws IOException {
        var length = inputStream.readInt();
        var bytes = new byte[length];
        inputStream.readFully(bytes);
        return new ByteArrayTag(bytes);
    }
}

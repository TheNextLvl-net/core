package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;

import java.io.IOException;

public class StringTag extends ValueTag<String> {
    public static final int ID = 8;

    public StringTag(String value) {
        super(value);
    }

    @Override
    public final boolean isString() {
        return true;
    }

    @Override
    public String getAsString() {
        return getValue();
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        var bytes = getValue().getBytes(outputStream.getCharset());
        outputStream.writeShort(bytes.length);
        outputStream.write(bytes);
    }

    public static StringTag read(NBTInputStream inputStream) throws IOException {
        var length = inputStream.readShort();
        var bytes = new byte[length];
        inputStream.readFully(bytes);
        var value = new String(bytes, inputStream.getCharset());
        return new StringTag(value);
    }
}

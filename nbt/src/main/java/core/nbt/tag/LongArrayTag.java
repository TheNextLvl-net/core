package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;

import java.io.IOException;

public class LongArrayTag extends ValueTag<long[]> implements IterableTag<Long> {
    public static final int ID = 12;

    public LongArrayTag(long[] value) {
        super(value);
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
    public Long get(int index) {
        return getValue()[index];
    }

    @Override
    public void set(int index, Long element) {
        getValue()[index] = element;
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeLong(getValue().length);
        for (var l : getValue()) outputStream.writeLong(l);
    }

    public static LongArrayTag read(NBTInputStream inputStream) throws IOException {
        var length = inputStream.readInt();
        var array = new long[length];
        for (var i = 0; i < length; i++)
            array[i] = inputStream.readLong();
        return new LongArrayTag(array);
    }
}

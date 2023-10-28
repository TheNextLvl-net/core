package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;

import java.io.IOException;

public class IntArrayTag extends ValueTag<int[]> implements IterableTag<Integer> {
    public static final int ID = 11;

    public IntArrayTag(int[] value) {
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
    public Integer get(int index) {
        return getValue()[index];
    }

    @Override
    public void set(int index, Integer element) {
        getValue()[index] = element;
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeInt(getValue().length);
        for (var i : getValue()) outputStream.writeInt(i);
    }

    public static IntArrayTag read(NBTInputStream inputStream) throws IOException {
        var length = inputStream.readInt();
        var array = new int[length];
        for (var i = 0; i < length; i++) array[i] = inputStream.readInt();
        return new IntArrayTag(array);
    }
}

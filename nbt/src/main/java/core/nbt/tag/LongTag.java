package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public class LongTag extends NumberTag<Long> {
    public static final int ID = 4;

    public LongTag(Long value) {
        super(value);
    }

    @Override
    public long getAsLong() {
        return getValue();
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeLong(getValue());
    }

    public static LongTag read(NBTInputStream inputStream) throws IOException {
        return new LongTag(inputStream.readLong());
    }
}

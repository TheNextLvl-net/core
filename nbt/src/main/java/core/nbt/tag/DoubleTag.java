package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public class DoubleTag extends NumberTag<Double> {
    public static final int ID = 6;

    public DoubleTag(Double value) {
        super(value);
    }

    @Override
    public double getAsDouble() {
        return getValue();
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeDouble(getValue());
    }

    public static DoubleTag read(NBTInputStream inputStream) throws IOException {
        return new DoubleTag(inputStream.readDouble());
    }
}

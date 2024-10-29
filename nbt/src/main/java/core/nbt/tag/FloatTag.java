package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public class FloatTag extends NumberTag<Float> {
    public static final int ID = 5;

    public FloatTag(Float value) {
        super(value);
    }

    @Override
    public float getAsFloat() {
        return getValue();
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeFloat(getValue());
    }

    public static FloatTag read(NBTInputStream inputStream) throws IOException {
        return new FloatTag(inputStream.readFloat());
    }
}

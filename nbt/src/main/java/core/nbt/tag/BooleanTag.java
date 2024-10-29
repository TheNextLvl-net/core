package core.nbt.tag;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class BooleanTag extends ByteTag {
    public BooleanTag(boolean value) {
        super((byte) (value ? 1 : 0));
    }

    public void setValue(boolean value) {
        setValue((byte) (value ? 1 : 0));
    }

    @Override
    public void setValue(Byte value) {
        if (value == 0 || value == 1) super.setValue(value);
        throw new IllegalArgumentException("value must be 0 or 1 but got: " + value);
    }

    @Override
    public final boolean isBoolean() {
        return true;
    }
}

package core.nbt.tag;

import core.nbt.NBTOutputStream;

import java.io.IOException;

public abstract class Tag {

    /**
     * @return the type id of this tag
     */
    public abstract int getTypeId();

    public abstract void write(NBTOutputStream outputStream) throws IOException;

    public boolean isCompound() {
        return this instanceof CompoundTag;
    }

    public boolean isList() {
        return this instanceof ListTag<?>;
    }

    public boolean isNumber() {
        return this instanceof NumberTag<?>;
    }

    public boolean isBoolean() {
        return this instanceof BooleanTag;
    }

    public boolean isString() {
        return this instanceof StringTag;
    }

    public CompoundTag getAsCompound() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public <V extends Tag> ListTag<V> getAsList() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public Number getAsNumber() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public String getAsString() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public boolean getAsBoolean() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public double getAsDouble() {
        return getAsNumber().doubleValue();
    }

    public float getAsFloat() {
        return getAsNumber().floatValue();
    }

    public long getAsLong() {
        return getAsNumber().longValue();
    }

    public int getAsInt() {
        return getAsNumber().intValue();
    }

    public byte getAsByte() {
        return getAsNumber().byteValue();
    }

    public short getAsShort() {
        return getAsNumber().shortValue();
    }
}

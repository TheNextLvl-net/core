package core.nbt.tag;

import core.nbt.NBTOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public abstract class Tag {

    /**
     * @return the type id of this tag
     */
    public abstract int getTypeId();

    public abstract void write(@NotNull NBTOutputStream outputStream) throws IOException;

    public boolean isCompound() {
        return this instanceof CompoundTag;
    }

    public boolean isList() {
        return this instanceof ListTag<?>;
    }

    public boolean isNumber() {
        return this instanceof ValueTag;
    }

    public boolean isString() {
        return this instanceof StringTag;
    }

    public CompoundTag getAsCompound() {
        return (CompoundTag) this;
    }

    public <E extends Tag> ListTag<E> getAsList() {
        return (ListTag<E>) this;
    }

    public Number getAsNumber() {
        return ((ValueTag<Number>) this).getValue();
    }

    public String getAsString() {
        return ((StringTag) this).getValue();
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

    public boolean getAsBoolean() {
        return getAsByte() == 1;
    }
}

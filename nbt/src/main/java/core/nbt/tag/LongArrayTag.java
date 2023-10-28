package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Getter
@Setter
@ToString
public class LongArrayTag extends ValueTag<long[]> {
    public static final int ID = 12;

    public LongArrayTag(long[] value) {
        super(value);
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
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

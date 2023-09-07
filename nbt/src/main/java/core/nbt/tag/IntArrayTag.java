package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Setter
@Getter
@ToString
public class IntArrayTag extends ValueTag<int[]> {
    public static final int ID = 11;

    public IntArrayTag(@Nullable String name, int[] value) {
        super(name, value);
    }

    public IntArrayTag(int[] value) {
        super(value);
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeInt(getValue().length);
        for (var i : getValue()) outputStream.writeInt(i);
    }
}

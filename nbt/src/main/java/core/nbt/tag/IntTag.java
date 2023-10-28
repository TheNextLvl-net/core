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
public class IntTag extends ValueTag<Integer> {
    public static final int ID = 3;

    public IntTag(Integer value) {
        super(value);
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeInt(getValue());
    }
}

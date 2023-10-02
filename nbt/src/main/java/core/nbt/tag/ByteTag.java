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
public class ByteTag extends ValueTag<Byte> {
    public static final int ID = 1;

    public ByteTag(@Nullable String name, Byte value) {
        super(name, value);
    }

    public ByteTag(Boolean value) {
        this((byte) (value ? 1 : 0));
    }

    public ByteTag(Byte value) {
        super(value);
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.write(getValue());
    }
}

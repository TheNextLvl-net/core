package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ByteTag implements NumberTag {
    public static final int ID = 1;
    private final @Nullable String name;
    private Byte value;

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.write(getValue());
    }
}

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
public class ByteArrayTag extends ValueTag<byte[]> {
    public static final int ID = 7;

    public ByteArrayTag(byte[] array) {
        super(array);
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeInt(getValue().length);
        outputStream.write(getValue());
    }

    public static ByteArrayTag read(NBTInputStream inputStream) throws IOException {
        var length = inputStream.readInt();
        var bytes = new byte[length];
        inputStream.readFully(bytes);
        return new ByteArrayTag(bytes);
    }
}

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
public class StringTag extends ValueTag<String> {
    public static final int ID = 8;

    public StringTag(@Nullable String name, String value) {
        super(name, value);
    }

    public StringTag(String value) {
        super(value);
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        var bytes = getValue().getBytes(outputStream.getCharset());
        outputStream.writeShort(bytes.length);
        outputStream.write(bytes);
    }
}

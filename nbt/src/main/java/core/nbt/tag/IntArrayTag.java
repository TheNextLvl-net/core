package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class IntArrayTag implements Tag {
    public static final int ID = 11;
    private final @Nullable String name;
    private int[] value;

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

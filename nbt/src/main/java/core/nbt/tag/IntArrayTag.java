package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Setter
@Getter
@AllArgsConstructor
public class IntArrayTag implements Tag {
    public static final Type TYPE = new Type("IntArrayTag", 11);
    private final @Nullable String name;
    private int[] value;

    @Override
    public @NotNull Type getType() {
        return TYPE;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeInt(getValue().length);
        for (var i : getValue()) outputStream.writeInt(i);
    }

    @Override
    public String toString() {
        var array = new StringBuilder();
        for (var b : getValue()) array.append(b).append(" ");
        return description().append("(int[]) [").append(array).append("]").toString();
    }
}

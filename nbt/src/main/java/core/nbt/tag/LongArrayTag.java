package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Getter
@Setter
@AllArgsConstructor
public class LongArrayTag implements Tag {
    public static final Type TYPE = new Type("LongArrayTag", 12);
    private final @Nullable String name;
    private long[] longs;

    @Override
    public @NotNull Type getType() {
        return TYPE;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeLong(getLongs().length);
        for (var l : getLongs()) outputStream.writeLong(l);
    }

    @Override
    public String toString() {
        var array = new StringBuilder();
        for (var l : getLongs()) array.append(l).append(" ");
        return description().append("(long[]) ").append(array).toString();
    }
}

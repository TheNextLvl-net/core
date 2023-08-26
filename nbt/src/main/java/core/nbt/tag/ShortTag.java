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
public class ShortTag implements Tag {
    public static final Type TYPE = new Type("ShortTag", 2);
    private final @Nullable String name;
    private short value;

    @Override
    public @NotNull Type getType() {
        return TYPE;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeShort(getValue());
    }

    @Override
    public String toString() {
        return description().append("(short) ").append(getValue()).toString();
    }
}

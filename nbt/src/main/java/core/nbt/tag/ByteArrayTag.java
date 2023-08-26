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
public class ByteArrayTag implements Tag {
    public static final Type TYPE = new Type("ByteArrayTag", 7);
    private final @Nullable String name;
    private byte[] bytes;

    @Override
    public @NotNull Type getType() {
        return TYPE;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeInt(getBytes().length);
        outputStream.write(getBytes());
    }

    @Override
    public String toString() {
        var hex = new StringBuilder();
        for (var b : getBytes()) {
            var hexDigits = Integer.toHexString(b).toUpperCase();
            if (hexDigits.length() == 1) hex.append("0");
            hex.append(hexDigits).append(" ");
        }
        return description().append("(byte[]) [").append(hex).append("]").toString();
    }
}

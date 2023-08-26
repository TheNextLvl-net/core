package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
@AllArgsConstructor
public class StringTag implements Tag {
    public static final Type TYPE = new Type("StringTag", 8);
    private final @Nullable String name;
    private String value;

    @Override
    public @NotNull Type getType() {
        return TYPE;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        var bytes = getValue().getBytes(StandardCharsets.UTF_8);
        outputStream.writeShort(bytes.length);
        outputStream.write(bytes);
    }

    @Override
    public String toString() {
        return description().append("\"").append(getValue()).append("\"").toString();
    }
}

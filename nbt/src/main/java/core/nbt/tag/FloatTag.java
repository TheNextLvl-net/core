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
public class FloatTag implements Tag {
    public static final Type TYPE = new Type("FloatTag", 5);
    private final @Nullable String name;
    private float value;

    @Override
    public @NotNull Type getType() {
        return TYPE;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeFloat(getValue());
    }

    @Override
    public String toString() {
        return description().append("(float) ").append(getValue()).toString();
    }
}

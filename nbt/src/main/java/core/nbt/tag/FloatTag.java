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
public class FloatTag extends ValueTag<Float> {
    public static final int ID = 5;

    public FloatTag(@Nullable String name, Float value) {
        super(name, value);
    }

    public FloatTag(Float value) {
        super(value);
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeFloat(getValue());
    }
}

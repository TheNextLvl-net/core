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
public class DoubleTag extends ValueTag<Double> {
    public static final int ID = 6;

    public DoubleTag(@Nullable String name, Double value) {
        super(name, value);
    }

    public DoubleTag(Double value) {
        super(value);
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeDouble(getValue());
    }
}

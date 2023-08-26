package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString(callSuper = true)
public class CompoundTag extends HashMap<String, Tag> implements Tag {
    public static final int ID = 10;
    private final @Nullable String name;

    public CompoundTag(@Nullable String name, Map<? extends String, ? extends Tag> map) {
        super(map);
        this.name = name;
    }

    public CompoundTag(@Nullable String name) {
        this.name = name;
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        for (var tag : values()) outputStream.writeTag(tag);
        EscapeTag.INSTANCE.write(outputStream);
    }
}

package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString(callSuper = true)
public class ListTag<V> extends ValueTag<Collection<? extends Tag>> {
    public static final int ID = 9;
    private final int contentTypeId;

    public ListTag(@Nullable String name, Collection<? extends Tag> value, int contentTypeId) {
        super(name, value);
        this.contentTypeId = contentTypeId;
    }

    public ListTag(Collection<? extends Tag> value, int contentTypeId) {
        super(value);
        this.contentTypeId = contentTypeId;
    }

    public ListTag(@Nullable String name, int contentTypeId) {
        super(name, new ArrayList<>());
        this.contentTypeId = contentTypeId;
    }

    public ListTag(int contentTypeId) {
        super(new ArrayList<>());
        this.contentTypeId = contentTypeId;
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeByte(getContentTypeId());
        outputStream.writeInt(getValue().size());
        for (var tag : getValue()) tag.write(outputStream);
    }
}

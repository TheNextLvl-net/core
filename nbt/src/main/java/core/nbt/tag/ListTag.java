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
public class ListTag<V> extends ArrayList<Tag> implements Tag {
    public static final int ID = 9;
    private final @Nullable String name;
    private final int contentTypeId;

    public ListTag(@Nullable String name, int contentTypeId, Collection<? extends Tag> collection) {
        super(collection);
        this.name = name;
        this.contentTypeId = contentTypeId;
    }

    public ListTag(@Nullable String name, int contentTypeId) {
        this.name = name;
        this.contentTypeId = contentTypeId;
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        outputStream.writeByte(getContentTypeId());
        outputStream.writeInt(size());
        for (var tag : this) tag.write(outputStream);
    }
}

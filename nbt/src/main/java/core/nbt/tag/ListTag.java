package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
@Setter
@ToString(callSuper = true)
public class ListTag<V extends Tag> extends ValueTag<List<V>> {
    public static final int ID = 9;
    private final int contentTypeId;

    public ListTag(List<V> value, int contentTypeId) {
        super(value);
        this.contentTypeId = contentTypeId;
    }

    @Override
    public final boolean isList() {
        return true;
    }

    @Override
    public ListTag<V> getAsList() {
        return this;
    }

    public ListTag(int contentTypeId) {
        super(new ArrayList<>());
        this.contentTypeId = contentTypeId;
    }

    public V get(int index) {
        return getValue().get(index);
    }

    public boolean contains(V v) {
        return getValue().contains(v);
    }

    public boolean add(V v) {
        return getValue().add(v);
    }

    public boolean remove(V v) {
        return getValue().remove(v);
    }

    public int size() {
        return getValue().size();
    }

    public boolean isEmpty() {
        return getValue().isEmpty();
    }

    public void forEach(Consumer<? super V> action) {
        getValue().forEach(action);
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeByte(getContentTypeId());
        outputStream.writeInt(getValue().size());
        for (var tag : getValue()) tag.write(outputStream);
    }

    public static <V extends Tag> ListTag<V> read(NBTInputStream inputStream) throws IOException {
        var type = inputStream.readByte();
        var length = inputStream.readInt();
        var list = new ArrayList<V>();
        for (var i = 0; i < length; i++) list.add((V) inputStream.readTag(type));
        return new ListTag<>(list, type);
    }
}

package core.nbt.tag;

import core.nbt.NBTOutputStream;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

@Getter
@ToString(callSuper = true)
public class CompoundTag extends ValueTag<Map<String, Tag>> {
    public static final int ID = 10;

    public CompoundTag(Map<String, Tag> value) {
        super(value);
    }

    public CompoundTag() {
        super(new HashMap<>());
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public void write(@NotNull NBTOutputStream outputStream) throws IOException {
        for (var tag : getValue().values()) outputStream.writeTag(tag);
        EscapeTag.INSTANCE.write(outputStream);
    }

    public void add(String name, Tag tag) {
        getValue().put(name, tag);
    }

    public Tag remove(String property) {
        return getValue().remove(property);
    }

    public void add(String name, String value) {
        add(name, new StringTag(name, value));
    }

    public void add(String name, Number number) {
        if (number instanceof Integer value) add(name, new IntTag(name, value));
        else if (number instanceof Float value) add(name, new FloatTag(name, value));
        else if (number instanceof Short value) add(name, new ShortTag(name, value));
        else if (number instanceof Long value) add(name, new LongTag(name, value));
        else if (number instanceof Byte value) add(name, new ByteTag(name, value));
        else add(name, new DoubleTag(name, number.doubleValue()));
    }

    public void add(String property, Boolean value) {
        add(property, new ByteTag(property, value ? (byte) 1 : 0));
    }

    public void add(Tag tag) {
        add(Objects.requireNonNull(tag.getName(), "name"), tag);
    }

    public void addAll(CompoundTag tag) {
        tag.forEach(this::add);
    }

    public void forEach(BiConsumer<? super String, ? super Tag> action) {
        getValue().forEach(action);
    }

    public Set<Map.Entry<String, Tag>> entrySet() {
        return getValue().entrySet();
    }

    public Set<String> keySet() {
        return getValue().keySet();
    }

    public int size() {
        return getValue().size();
    }

    public boolean containsKey(String property) {
        return getValue().containsKey(property);
    }

    public Tag get(String property) {
        return getValue().get(property);
    }

    public <E extends Tag> ListTag<E> getAsList(String tag) {
        return getValue().get(tag).getAsList();
    }

    public CompoundTag getAsCompound(String tag) {
        return getValue().get(tag).getAsCompound();
    }
}

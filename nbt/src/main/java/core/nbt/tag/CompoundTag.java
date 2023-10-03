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

@Getter
@ToString(callSuper = true)
public class CompoundTag extends ValueTag<Map<String, Tag>> {
    public static final int ID = 10;

    public CompoundTag(@Nullable String name, Map<String, Tag> value) {
        super(name, value);
    }

    public CompoundTag(Map<String, Tag> value) {
        super(value);
    }

    public CompoundTag(String name) {
        super(name, new HashMap<>());
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

    public void put(String name, Tag tag) {
        getValue().put(name, tag);
    }

    public Tag remove(String property) {
        return getValue().remove(property);
    }

    public void put(String name, String value) {
        put(name, new StringTag(name, value));
    }

    public void put(String name, Number number) {
        if (number instanceof Integer value) put(name, new IntTag(name, value));
        else if (number instanceof Float value) put(name, new FloatTag(name, value));
        else if (number instanceof Short value) put(name, new ShortTag(name, value));
        else if (number instanceof Long value) put(name, new LongTag(name, value));
        else if (number instanceof Byte value) put(name, new ByteTag(name, value));
        else put(name, new DoubleTag(name, number.doubleValue()));
    }

    public void put(String property, Boolean value) {
        put(property, new ByteTag(property, value ? (byte) 1 : 0));
    }

    public void add(Tag tag) {
        put(Objects.requireNonNull(tag.getName(), "name"), tag);
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

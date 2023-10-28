package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class CompoundTag extends ValueTag<Map<String, Tag>> {
    public static final int ID = 10;

    public CompoundTag(Map<String, Tag> value) {
        super(value);
    }

    public CompoundTag() {
        this(new HashMap<>());
    }

    @Override
    public final boolean isCompound() {
        return true;
    }

    @Override
    public CompoundTag getAsCompound() {
        return this;
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    public void add(String name, Tag tag) {
        getValue().put(name, tag);
    }

    public Tag remove(String name) {
        return getValue().remove(name);
    }

    public void add(String name, String value) {
        add(name, new StringTag(value));
    }

    public void add(String name, Number number) {
        if (number instanceof Integer value) add(name, new IntTag(value));
        else if (number instanceof Float value) add(name, new FloatTag(value));
        else if (number instanceof Short value) add(name, new ShortTag(value));
        else if (number instanceof Long value) add(name, new LongTag(value));
        else if (number instanceof Byte value) add(name, new ByteTag(value));
        else add(name, new DoubleTag(number.doubleValue()));
    }

    public void add(String name, Boolean value) {
        add(name, new ByteTag(value ? (byte) 1 : 0));
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

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        for (var entry : entrySet()) outputStream.writeTag(entry.getKey(), entry.getValue());
        EscapeTag.INSTANCE.write(outputStream);
    }

    public static CompoundTag read(NBTInputStream inputStream) throws IOException {
        var value = new HashMap<String, Tag>();
        while (true) {
            var entry = inputStream.readNamedTag();
            if (entry.getValue().isEmpty()) break;
            value.put(entry.getValue().get(), entry.getKey());
        }
        return new CompoundTag(value);
    }
}

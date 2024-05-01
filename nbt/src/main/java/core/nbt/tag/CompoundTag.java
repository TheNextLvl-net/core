package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
        switch (number) {
            case Integer value -> add(name, new IntTag(value));
            case Float value -> add(name, new FloatTag(value));
            case Short value -> add(name, new ShortTag(value));
            case Long value -> add(name, new LongTag(value));
            case Byte value -> add(name, new ByteTag(value));
            default -> add(name, new DoubleTag(number.doubleValue()));
        }
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

    @SuppressWarnings("unchecked")
    public @Nullable <T extends Tag> T get(String property) {
        return (T) getValue().get(property);
    }

    public <E extends Tag> ListTag<E> getAsList(String tag) {
        return getValue().get(tag).getAsList();
    }

    public CompoundTag getAsCompound(String tag) {
        return getValue().get(tag).getAsCompound();
    }

    @SuppressWarnings("unchecked")
    public <T extends Tag> T getOrAdd(String tag, T defaultValue) {
        var value = get(tag);
        if (value != null) return (T) value;
        add(tag, defaultValue);
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    public <T extends Tag> T getOrDefault(String tag, T defaultValue) {
        return (T) getValue().getOrDefault(tag, defaultValue);
    }

    public <T extends Tag> Optional<T> optional(String tag) {
        return Optional.ofNullable(get(tag));
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

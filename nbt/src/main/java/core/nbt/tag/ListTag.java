package core.nbt.tag;

import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.IOException;
import java.util.*;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ListTag<V extends Tag> extends ValueTag<List<V>> implements List<V> {
    public static final int ID = 9;
    private final int contentTypeId;

    public ListTag(List<V> value, int contentTypeId) {
        super(value);
        this.contentTypeId = contentTypeId;
    }

    public ListTag(int contentTypeId) {
        this(new ArrayList<>(), contentTypeId);
    }

    @Override
    public final boolean isList() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ListTag<V> getAsList() {
        return this;
    }

    @Override
    public int getTypeId() {
        return ID;
    }

    @Override
    public int size() {
        return getValue().size();
    }

    @Override
    public boolean isEmpty() {
        return getValue().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return getValue().contains(o);
    }

    @Override
    public Iterator<V> iterator() {
        return getValue().iterator();
    }

    @Override
    public Object[] toArray() {
        return getValue().toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return getValue().toArray(ts);
    }

    @Override
    public boolean add(V v) {
        return getValue().add(v);
    }

    @Override
    public boolean remove(Object o) {
        return getValue().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return new HashSet<>(getValue()).containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends V> collection) {
        return getValue().addAll(collection);
    }

    @Override
    public boolean addAll(int i, Collection<? extends V> collection) {
        return getValue().addAll(i, collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return getValue().removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return getValue().retainAll(collection);
    }

    @Override
    public void clear() {
        getValue().clear();
    }

    @Override
    public V get(int i) {
        return getValue().get(i);
    }

    @Override
    public V set(int i, V v) {
        return getValue().set(i, v);
    }

    @Override
    public void add(int i, V v) {
        getValue().add(i, v);
    }

    @Override
    public V remove(int i) {
        return getValue().remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return getValue().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return getValue().lastIndexOf(o);
    }

    @Override
    public ListIterator<V> listIterator() {
        return getValue().listIterator();
    }

    @Override
    public ListIterator<V> listIterator(int i) {
        return getValue().listIterator(i);
    }

    @Override
    public List<V> subList(int i, int i1) {
        return getValue().subList(i, i1);
    }

    @Override
    public String toString() {
        return "ListTag{" +
                "contentTypeId=" + contentTypeId +
                ", value=" + super.getValue() +
                '}';
    }

    @Override
    public void write(NBTOutputStream outputStream) throws IOException {
        outputStream.writeByte(getContentTypeId());
        outputStream.writeInt(getValue().size());
        for (var tag : getValue()) tag.write(outputStream);
    }

    @SuppressWarnings("unchecked")
    public static <V extends Tag> ListTag<V> read(NBTInputStream inputStream) throws IOException {
        var type = inputStream.readByte();
        var length = inputStream.readInt();
        var list = new ArrayList<V>();
        for (var i = 0; i < length; i++) list.add((V) inputStream.readTag(type));
        return new ListTag<>(list, type);
    }
}

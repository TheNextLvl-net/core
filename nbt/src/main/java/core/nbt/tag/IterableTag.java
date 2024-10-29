package core.nbt.tag;

import org.jspecify.annotations.NullMarked;

import java.util.Iterator;

@NullMarked
public interface IterableTag<E> extends Iterable<E> {

    int size();

    E get(int index);

    void set(int index, E element);

    @Override
    default Iterator<E> iterator() {
        return new Iterator<>() {
            private int index;

            @Override
            public boolean hasNext() {
                return index < IterableTag.this.size();
            }

            @Override
            public E next() {
                return IterableTag.this.get(index++);
            }
        };
    }
}

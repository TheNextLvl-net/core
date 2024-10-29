package core.nbt.tag;

import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class NumberTag<T extends Number> extends ValueTag<T> {
    public NumberTag(T number) {
        super(number);
    }

    @Override
    public final boolean isNumber() {
        return true;
    }

    @Override
    public Number getAsNumber() {
        return getValue();
    }
}

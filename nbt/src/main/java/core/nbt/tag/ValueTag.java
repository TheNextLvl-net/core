package core.nbt.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
public abstract class ValueTag<T> extends Tag {
    private @Nullable String name;
    private T value;

    @Override
    public String getAsString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        var valueTag = (ValueTag<?>) object;
        return Objects.equals(getValue(), valueTag.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{" +
                "value=" + value +
                '}';
    }
}

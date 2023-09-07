package core.nbt.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
public abstract class ValueTag<T> extends Tag {
    private final @Nullable String name;
    private T value;

    public ValueTag(T value) {
        this(null, value);
    }
}

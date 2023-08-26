package core.api.placeholder;

import java.util.ArrayList;
import java.util.List;

@Deprecated(forRemoval = true, since = "3.1.14")
public final class MessageKey<I> extends Key<I> {
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static final List<MessageKey<?>> LIST = new ArrayList<>();

    @Deprecated(forRemoval = true, since = "3.1.14")
    public MessageKey(String key, Placeholder.Formatter<? super I> formatter) {
        super(key, formatter);
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public MessageKey<I> register() {
        if (!LIST.contains(this)) LIST.add(this);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public void unregister() {
        LIST.remove(this);
    }
}

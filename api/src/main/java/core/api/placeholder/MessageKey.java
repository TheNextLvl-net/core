package core.api.placeholder;

import java.util.ArrayList;
import java.util.List;

public final class MessageKey<I> extends Key<I> {
    public static final List<MessageKey<?>> LIST = new ArrayList<>();

    public MessageKey(String key, Placeholder.Formatter<? super I> formatter) {
        super(key, formatter);
    }

    public MessageKey<I> register() {
        if (!LIST.contains(this)) LIST.add(this);
        return this;
    }

    public void unregister() {
        LIST.remove(this);
    }
}

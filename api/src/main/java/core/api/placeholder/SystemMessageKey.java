package core.api.placeholder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class SystemMessageKey<I> extends Key<I> {
    public static final List<SystemMessageKey<?>> LIST = new ArrayList<>();

    public static final SystemMessageKey<?> LOG_INFO = new SystemMessageKey<>("log.info", Placeholder.Formatter.DEFAULT).register();
    public static final SystemMessageKey<?> LOG_WARN = new SystemMessageKey<>("log.warn", Placeholder.Formatter.DEFAULT).register();
    public static final SystemMessageKey<?> LOG_ERROR = new SystemMessageKey<>("log.error", Placeholder.Formatter.DEFAULT).register();
    public static final SystemMessageKey<?> LOG_TRACE = new SystemMessageKey<>("log.trace", Placeholder.Formatter.DEFAULT).register();
    public static final SystemMessageKey<?> LOG_DEBUG = new SystemMessageKey<>("log.debug", Placeholder.Formatter.DEFAULT).register();

    public SystemMessageKey(String key, Placeholder.Formatter<? super I> formatter) {
        super(key, formatter);
    }

    public SystemMessageKey<I> register() {
        if (!LIST.contains(this)) LIST.add(this);
        return this;
    }

    public void unregister() {
        LIST.remove(this);
    }

    @SafeVarargs
    public final String message(Placeholder<? extends I>... placeholders) {
        return super.message(Locale.ROOT, placeholders);
    }

    @SafeVarargs
    public final String message(@Nullable I input, Placeholder<? extends I>... placeholders) {
        return super.message(Locale.ROOT, input, placeholders);
    }
}

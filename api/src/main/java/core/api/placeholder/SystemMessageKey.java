package core.api.placeholder;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Deprecated(forRemoval = true, since = "3.1.14")
public final class SystemMessageKey<I> extends Key<I> {
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static final List<SystemMessageKey<?>> LIST = new ArrayList<>();

    @Deprecated(forRemoval = true, since = "3.1.14")
    public static final SystemMessageKey<?> LOG_INFO = new SystemMessageKey<>("log.info", Placeholder.Formatter.DEFAULT).register();
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static final SystemMessageKey<?> LOG_WARN = new SystemMessageKey<>("log.warn", Placeholder.Formatter.DEFAULT).register();
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static final SystemMessageKey<?> LOG_ERROR = new SystemMessageKey<>("log.error", Placeholder.Formatter.DEFAULT).register();
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static final SystemMessageKey<?> LOG_TRACE = new SystemMessageKey<>("log.trace", Placeholder.Formatter.DEFAULT).register();
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static final SystemMessageKey<?> LOG_DEBUG = new SystemMessageKey<>("log.debug", Placeholder.Formatter.DEFAULT).register();

    @Deprecated(forRemoval = true, since = "3.1.14")
    public SystemMessageKey(String key, Placeholder.Formatter<? super I> formatter) {
        super(key, formatter);
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public SystemMessageKey<I> register() {
        if (!LIST.contains(this)) LIST.add(this);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public void unregister() {
        LIST.remove(this);
    }

    @SafeVarargs
    @Deprecated(forRemoval = true, since = "3.1.14")
    public final String message(Placeholder<? extends I>... placeholders) {
        return super.message(Locale.ROOT, placeholders);
    }

    @SafeVarargs
    @Deprecated(forRemoval = true, since = "3.1.14")
    public final String message(@Nullable I input, Placeholder<? extends I>... placeholders) {
        return super.message(Locale.ROOT, input, placeholders);
    }
}

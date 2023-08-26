package core.api.placeholder;

import core.api.file.format.MessageFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@Getter
@Accessors(makeFinal = true, fluent = true)
@Deprecated(forRemoval = true, since = "3.1.14")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Key<I> {
    @Deprecated(forRemoval = true, since = "3.1.14")
    private final String key;
    @Deprecated(forRemoval = true, since = "3.1.14")
    private final Placeholder.Formatter<? super I> formatter;

    @Deprecated(forRemoval = true, since = "3.1.14")
    protected abstract Key<I> register();

    @Deprecated(forRemoval = true, since = "3.1.14")
    protected abstract void unregister();

    @SuppressWarnings("unchecked")
    @Deprecated(forRemoval = true, since = "3.1.14")
    public String message(Locale locale, Placeholder<? extends I>... placeholders) {
        return message(locale, null, placeholders);
    }

    @SuppressWarnings("unchecked")
    @Deprecated(forRemoval = true, since = "3.1.14")
    public String message(Locale locale, @Nullable I input, Placeholder<? extends I>... placeholders) {
        if (!MessageFile.isRegistered(locale)) return "No translations found for " + locale;
        var file = MessageFile.getFile(locale);
        String message = file.getMessage(this, file.getFile().getName() + "$" + key());
        return formatter().format(message, input, placeholders);
    }
}

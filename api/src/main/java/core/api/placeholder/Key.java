package core.api.placeholder;

import core.api.file.format.MessageFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.util.Locale;

@Getter
@Accessors(makeFinal = true, fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Key<I> {
    private final String key;
    private final Placeholder.Formatter<? super I> formatter;

    protected abstract Key<I> register();

    protected abstract void unregister();

    @SuppressWarnings("unchecked")
    public String message(Locale locale, Placeholder<? extends I>... placeholders) {
        return message(locale, null, placeholders);
    }

    @SuppressWarnings("unchecked")
    public String message(Locale locale, @Nullable I input, Placeholder<? extends I>... placeholders) {
        if (!MessageFile.isRegistered(locale)) return "No translations found for " + locale;
        var file = MessageFile.getFile(locale);
        String message = file.getMessage(this, file.getFile().getName() + "$" + key());
        return formatter().format(message, input, placeholders);
    }
}

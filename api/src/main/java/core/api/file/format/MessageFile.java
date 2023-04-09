package core.api.file.format;

import core.api.placeholder.Key;
import core.api.placeholder.SystemMessageKey;
import lombok.Getter;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;

@Getter
public class MessageFile extends JsonFile {
    public static final File DATA_FOLDER = new File("core", "messages");
    public static final HashMap<String, MessageFile> FILES = new HashMap<>();
    public static final MessageFile ROOT = new MessageFile("root.json", Locale.ROOT).register();
    private final Locale locale;

    static {
        File[] files = DATA_FOLDER.listFiles((file, s) -> s.endsWith(".json"));
        if (files != null) for (File file : files) {
            if (file.equals(MessageFile.ROOT.getFile())) continue;
            String tag = file.getName().substring(0, file.getName().length() - 5);
            Locale locale = Locale.forLanguageTag(tag.replace("_", "-"));
            new MessageFile(file.getName(), locale).register().save();
        }
    }

    static {
        ROOT.setDefault(SystemMessageKey.LOG_INFO, "§8[§1%time% §8|§1 Info §8| §1%thread%§8]");
        ROOT.setDefault(SystemMessageKey.LOG_WARN, "§8[§e%time% §8|§e Warning §8| §e%thread%§8]");
        ROOT.setDefault(SystemMessageKey.LOG_ERROR, "§8[§4%time% §8|§4 Error §8| §4%thread%§8]");
        ROOT.setDefault(SystemMessageKey.LOG_DEBUG, "§8[§6%time% §8|§6 Debug §8| §6%thread%§8]");
        ROOT.setDefault(SystemMessageKey.LOG_TRACE, "§8[§4%time% §8|§4 Trace §8| §4%thread%§8]");
        ROOT.save();
    }

    public MessageFile(String name, Locale locale) {
        super(new File(DATA_FOLDER, name));
        this.locale = locale;
    }

    public MessageFile(Locale locale) {
        this(locale.toString().concat(".json"), locale);
    }

    public boolean isSet(Key<?> key) {
        return getRoot().getAsJsonObject().has(key.key());
    }

    public void setDefault(Key<?> key, String message) {
        if (!isSet(key)) setMessage(key, message);
    }

    public void setMessage(Key<?> key, String message) {
        getRoot().getAsJsonObject().addProperty(key.key(), message);
    }

    @Nullable
    public String getMessage(Key<?> key) {
        return isSet(key) ? getRoot().getAsJsonObject().get(key.key()).getAsString() : null;
    }

    public String getMessage(Key<?> key, String defaultValue) {
        return isSet(key) ? getRoot().getAsJsonObject().get(key.key()).getAsString() : defaultValue;
    }

    public MessageFile register() {
        FILES.putIfAbsent(getLocale().toLanguageTag(), this);
        return this;
    }

    public void unregister() {
        FILES.remove(getLocale().toLanguageTag(), this);
    }

    public static MessageFile getOrCreate(Locale locale) {
        return isRegistered(locale) ? getFile(locale) : new MessageFile(locale).register();
    }

    public static MessageFile getFile(Locale locale) throws IllegalStateException {
        if (isRegistered(locale)) return FILES.get(locale.toLanguageTag());
        throw new IllegalStateException("No matching message file found for %s".formatted(locale));
    }

    public static boolean isRegistered(Locale locale) {
        return FILES.containsKey(locale.toLanguageTag());
    }

    public static Locale parseLocale(String tag) {
        var segments = tag.split("_", 3); // language_country_variant
        return switch (segments.length) {
            case 1 -> new Locale(tag); // language
            case 2 -> new Locale(segments[0], segments[1]); // language + country
            case 3 -> new Locale(segments[0], segments[1], segments[2]); // language + country + variant
            default -> Locale.forLanguageTag(tag.replace("_", "-"));
        };
    }
}

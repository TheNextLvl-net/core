package core.api.file.format;

import com.google.gson.JsonObject;
import core.api.placeholder.Key;
import core.api.placeholder.SystemMessageKey;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

@Getter
@Deprecated(forRemoval = true, since = "3.1.14")
public class MessageFile extends JsonFile<JsonObject> {
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static final File DATA_FOLDER = new File("core", "messages");
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static final HashMap<String, MessageFile> FILES = new HashMap<>();
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static final MessageFile ROOT = new MessageFile("root.json", Locale.ROOT).register();
    @Deprecated(forRemoval = true, since = "3.1.14")
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

    @Deprecated(forRemoval = true, since = "3.1.14")
    public MessageFile(String name, Locale locale) {
        super(new File(DATA_FOLDER, name));
        this.locale = locale;
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public MessageFile(Locale locale) {
        this(locale.toString().concat(".json"), locale);
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public boolean isSet(Key<?> key) {
        return getRoot().has(key.key());
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public void setDefault(Key<?> key, String message) {
        if (!isSet(key)) setMessage(key, message);
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public void setMessage(Key<?> key, String message) {
        getRoot().addProperty(key.key(), message);
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public @Nullable String getMessage(Key<?> key) {
        return isSet(key) ? getRoot().get(key.key()).getAsString() : null;
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public String getMessage(Key<?> key, String defaultValue) {
        return isSet(key) ? getRoot().get(key.key()).getAsString() : defaultValue;
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public MessageFile register() {
        FILES.putIfAbsent(getLocale().toLanguageTag(), this);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public void unregister() {
        FILES.remove(getLocale().toLanguageTag(), this);
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public static MessageFile getOrCreate(Locale locale) {
        return isRegistered(locale) ? getFile(locale) : new MessageFile(locale).register();
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public static MessageFile getFile(Locale locale) throws IllegalStateException {
        if (isRegistered(locale)) return FILES.get(locale.toLanguageTag());
        throw new IllegalStateException("No matching message file found for %s".formatted(locale));
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
    public static boolean isRegistered(Locale locale) {
        return FILES.containsKey(locale.toLanguageTag());
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
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

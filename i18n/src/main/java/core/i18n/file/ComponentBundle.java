package core.i18n.file;

import core.api.file.format.PropertiesFile;
import core.util.Properties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(fluent = true, chain = true)
public class ComponentBundle {
    private final Map<Locale, Properties> files = new HashMap<>();

    private final File directory;
    private final Charset charset;
    private final Function<Audience, Locale> mapping;

    private MiniMessage miniMessage = MiniMessage.miniMessage();
    private @Nullable Locale fallback;

    public ComponentBundle(File directory, Function<Audience, Locale> mapping) {
        this(directory, StandardCharsets.ISO_8859_1, mapping);
    }

    /**
     * Register a new bundle and save it if it does not exist
     *
     * @param baseName the base name of the resource bundle
     * @param locale   the locale for which the resource bundle is desired
     * @return the component bundle
     */
    public ComponentBundle register(String baseName, Locale locale) {
        var file = new PropertiesFile(new File(directory, baseName + ".properties"), charset, Properties.unordered());
        if (!file.getFile().exists()) {
            var bundle = ResourceBundle.getBundle(baseName, locale);
            bundle.keySet().forEach(key -> file.getRoot().set(key, bundle.getString(key)));
            files.put(locale, file.save().getRoot());
        } else files.put(locale, file.getRoot());
        return this;
    }

    /**
     * Get the format from a property key for a locale
     *
     * @param locale the locale
     * @param key    the key
     * @return the format
     */
    public String format(Locale locale, String key) {
        return files.getOrDefault(locale, files.get(fallback())).getString(key);
    }

    /**
     * Get the format from a property key for an audience
     *
     * @param audience the audience
     * @param key      the key
     * @return the format
     */
    public String format(Audience audience, String key) {
        return format(mapping().apply(audience), key);
    }

    /**
     * Get a deserialized component from a property key for a locale
     *
     * @param locale       the locale to get the input string for
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     * @return the {@link MiniMessage#deserialize(String, TagResolver...) deserialized} component
     */
    public Component component(Locale locale, String key, TagResolver... tagResolvers) {
        return miniMessage.deserialize(format(locale, key), tagResolvers);
    }

    /**
     * Get a deserialized component from a property key for an audience
     *
     * @param audience     the audience to geht the input string for
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags, last specified taking priority
     * @return the {@link MiniMessage#deserialize(String, TagResolver...) deserialized} component
     */
    public Component component(Audience audience, String key, TagResolver... tagResolvers) {
        return component(mapping().apply(audience), key, tagResolvers);
    }

    /**
     * Get a deserialized component from a property key for a locale
     *
     * @param locale       the locale to get the input string for
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     * @return the {@link MiniMessage#deserialize(String, TagResolver...) deserialized} component or null if empty
     */
    public @Nullable Component nullable(Locale locale, String key, TagResolver... tagResolvers) {
        var string = format(locale, key);
        return string.isEmpty() ? null : miniMessage.deserialize(string, tagResolvers);
    }

    /**
     * Get a deserialized component from a property key for a locale
     *
     * @param audience     the audience to get the input string for
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     * @return the {@link MiniMessage#deserialize(String, TagResolver...) deserialized} component or null if empty
     */
    public @Nullable Component nullable(Audience audience, String key, TagResolver... tagResolvers) {
        return nullable(mapping().apply(audience), key, tagResolvers);
    }

    /**
     * Send a message to an audience unless it is empty
     *
     * @param audience     the audience
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     */
    public void sendMessage(Audience audience, String key, TagResolver... tagResolvers) {
        var component = nullable(mapping.apply(audience), key, tagResolvers);
        if (component != null) audience.sendMessage(component);
    }
}

package core.i18n.file;

import core.file.format.PropertiesFile;
import core.io.IO;
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

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
    private Locale fallback = Locale.US;

    public ComponentBundle(File directory, Function<Audience, Locale> mapping) {
        this(directory, StandardCharsets.UTF_8, mapping);
    }

    /**
     * Register a new or merge with an existing bundle and save it if it does not exist
     *
     * @param baseName the base name of the resource bundle
     * @param locale   the locale for which the resource bundle is desired
     * @return the component bundle
     */
    public ComponentBundle register(String baseName, Locale locale) {
        try (var io = IO.ofResource(baseName + ".properties")) {
            var resource = io.isReadable() ? new Properties().read(
                    io.inputStream(StandardOpenOption.READ),
                    charset()
            ) : null;
            if (resource == null) throw new FileNotFoundException("Resource not found: " + baseName);
            var file = new PropertiesFile(IO.of(directory, baseName + ".properties"), charset, resource);
            files.compute(locale, (ignored, previous) -> {
                var root = file.validate().getRoot();
                if (previous != null) root.merge(previous);
                root.merge(resource);
                return file.save().getRoot();
            });
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the format from a property key for a locale
     *
     * @param locale the locale
     * @param key    the key
     * @return the format
     */
    public @Nullable String format(Locale locale, String key) {
        var request = files.get(locale);
        if (request != null && request.containsKey(key))
            return request.getProperty(key);
        var fallback = files.get(fallback());
        if (fallback != null && fallback.containsKey(key))
            return fallback.getProperty(key);
        return null;
    }

    /**
     * Get the format from a property key for an audience
     *
     * @param audience the audience
     * @param key      the key
     * @return the format
     */
    public @Nullable String format(Audience audience, String key) {
        return format(mapping().apply(audience), key);
    }

    /**
     * Get a deserialized component from a property key for a locale
     *
     * @param locale       the locale to get the input string for
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     * @return the {@link ComponentBundle#deserialize(String, TagResolver...) deserialized} component
     */
    public Component component(Locale locale, String key, TagResolver... tagResolvers) {
        var format = format(locale, key);
        if (format == null) return Component.text(key);
        return deserialize(format, tagResolvers);
    }

    /**
     * Get a deserialized component from a property key for an audience
     *
     * @param audience     the audience to get the input string for
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags, last specified taking priority
     * @return the {@link ComponentBundle#deserialize(String, TagResolver...) deserialized} component
     */
    public Component component(Audience audience, String key, TagResolver... tagResolvers) {
        return component(mapping().apply(audience), key, tagResolvers);
    }

    /**
     * Get a deserialized component array from a property key for a locale
     *
     * @param locale       the locale to get the input string for
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     * @return the {@link ComponentBundle#deserializeArray(String, TagResolver...) deserialized} component
     */
    public Component[] components(Locale locale, String key, TagResolver... tagResolvers) {
        var format = format(locale, key);
        if (format == null) return new Component[]{Component.text(key)};
        return deserializeArray(format, tagResolvers);
    }

    /**
     * Get a deserialized component array from a property key for an audience
     *
     * @param audience     the audience to get the input string for
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags, last specified taking priority
     * @return the {@link ComponentBundle#deserializeArray(String, TagResolver...) deserialized} component
     */
    public Component[] components(Audience audience, String key, TagResolver... tagResolvers) {
        return components(mapping().apply(audience), key, tagResolvers);
    }

    /**
     * Get a deserialized component from a property key for a locale
     *
     * @param locale       the locale to get the input string for
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     * @return the {@link ComponentBundle#deserialize(String, TagResolver...) deserialized} component or null if empty
     */
    public @Nullable Component nullable(Locale locale, String key, TagResolver... tagResolvers) {
        var format = format(locale, key);
        return format == null ? null : deserialize(format, tagResolvers);
    }

    /**
     * Get a deserialized component from a property key for a locale
     *
     * @param audience     the audience to get the input string for
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     * @return the {@link ComponentBundle#deserialize(String, TagResolver...) deserialized} component or null if empty
     */
    public @Nullable Component nullable(Audience audience, String key, TagResolver... tagResolvers) {
        return nullable(mapping().apply(audience), key, tagResolvers);
    }

    /**
     * Get a deserialized component from a raw message
     *
     * @param message      the message to deserialize
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     * @return the {@link MiniMessage#deserialize(String, TagResolver...) deserialized} component
     */
    public Component deserialize(String message, TagResolver... tagResolvers) {
        return miniMessage.deserialize(message, tagResolvers);
    }

    /**
     * Get a deserialized component array from a raw message
     *
     * @param message      the message to deserialize
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     * @return the {@link MiniMessage#deserialize(String, TagResolver...) deserialized} component
     */
    public Component[] deserializeArray(String message, TagResolver... tagResolvers) {
        return Arrays.stream(message.split("\n|<newline>"))
                .map(s -> deserialize(s, tagResolvers))
                .toList().toArray(new Component[]{});
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

    /**
     * Send a raw message to an audience unless it is empty
     *
     * @param audience     the audience
     * @param message      the message to send
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     */
    public void sendRawMessage(Audience audience, String message, TagResolver... tagResolvers) {
        if (!message.isEmpty()) audience.sendMessage(deserialize(message, tagResolvers));
    }
}

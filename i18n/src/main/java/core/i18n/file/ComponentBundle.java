package core.i18n.file;

import core.file.Validatable;
import core.file.format.PropertiesFile;
import core.io.IO;
import core.util.Properties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ComponentBundle {
    private final Map<Locale, Properties> files = new HashMap<>();

    private final File directory;
    private final Charset charset;
    private final Function<Audience, Locale> mapping;

    private Validatable.Scope scope = Validatable.Scope.FILTER_AND_FILL;
    private MiniMessage miniMessage = MiniMessage.miniMessage();
    private Locale fallback = Locale.US;

    public ComponentBundle(File directory, Function<Audience, Locale> mapping) {
        this(directory, StandardCharsets.UTF_8, mapping);
    }

    /**
     * Applies the MiniMessage function to the current ComponentBundle and sets the result as the new miniMessage.
     *
     * @param function the function that takes a ComponentBundle and returns a MiniMessage
     * @return the updated component bundle
     */
    public ComponentBundle miniMessage(Function<ComponentBundle, MiniMessage> function) {
        this.miniMessage = function.apply(this);
        return this;
    }

    /**
     * Adds this component bundle to the global translator.
     *
     * @param key the key for the translation registry
     * @return the component bundle
     */
    public ComponentBundle addGlobalTranslationSource(Key key) {
        var registry = TranslationRegistry.create(key);
        registry.defaultLocale(fallback());
        files().forEach((locale, properties) -> properties.forEach((property, value) ->
                registry.register(property.toString(), locale, new MessageFormat(value.toString()))));
        GlobalTranslator.translator().addSource(registry);
        return this;
    }

    /**
     * Adds only the given keys to the global translator.
     *
     * @param key  the key for the translation registry
     * @param keys the keys to add to the translation registry
     * @return the component bundle
     */
    public ComponentBundle addGlobalTranslationSource(Key key, String... keys) {
        return addGlobalTranslationSource(key, Set.of(keys));
    }

    /**
     * Adds only the given keys to the global translator.
     *
     * @param key  the key for the translation registry
     * @param keys the keys to add to the translation registry
     * @return the component bundle
     */
    public ComponentBundle addGlobalTranslationSource(Key key, Set<String> keys) {
        var registry = TranslationRegistry.create(key);
        registry.defaultLocale(fallback());
        files().forEach((locale, properties) -> keys.forEach(property -> {
            var value = properties.getProperty(property);
            registry.register(property, locale, new MessageFormat(value));
        }));
        GlobalTranslator.translator().addSource(registry);
        return this;
    }

    /**
     * Register a new or merge with an existing bundle and save it if it does not exist
     *
     * @param baseName the base name of the resource bundle
     * @param locale   the locale for which the resource bundle is desired
     * @return the component bundle
     */
    public ComponentBundle register(String baseName, Locale locale) {
        var qualifiedName = baseName + ".properties";
        try (var io = IO.ofResource(qualifiedName)) {
            var resource = io.isReadable() ? new Properties().read(
                    io.inputStream(StandardOpenOption.READ),
                    charset()
            ) : null;
            if (resource == null) throw new FileNotFoundException("Resource not found: " + qualifiedName);
            var file = new PropertiesFile(IO.of(directory, qualifiedName), charset, resource);
            files.compute(locale, (ignored, previous) -> {
                var root = file.validate(scope()).getRoot();
                if (previous != null) root.merge(previous);
                root.merge(resource);
                return file.getRoot().isEmpty() ? file.getRoot() : file.save().getRoot();
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
    @ApiStatus.Internal
    public @Nullable String format(Locale locale, String key) {
        var request = files.get(locale);
        if (request != null && request.containsKey(key))
            return request.getProperty(key);
        if (locale.equals(this.fallback))
            return null;
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
    @ApiStatus.Internal
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
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
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
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
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
    @ApiStatus.Internal
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
    @ApiStatus.Internal
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

    /**
     * Sends a title to the specified audience.
     *
     * @param audience     the audience to send the title to
     * @param title        the title to send
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     */
    public void sendTitle(Audience audience, String title, TagResolver... tagResolvers) {
        sendTitle(audience, title, null, null, tagResolvers);
    }

    /**
     * Sends a title to the specified audience.
     *
     * @param audience     the audience to send the title to
     * @param title        the title to send
     * @param times        the timings of the title
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     */
    public void sendTitle(Audience audience, String title, Title.Times times, TagResolver... tagResolvers) {
        sendTitle(audience, title, null, times, tagResolvers);
    }

    /**
     * Sends a subtitle to the specified audience.
     *
     * @param audience     the audience to send the subtitle to
     * @param subtitle     the subtitle to send
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     */
    public void sendSubtitle(Audience audience, String subtitle, TagResolver... tagResolvers) {
        sendTitle(audience, null, subtitle, null, tagResolvers);
    }

    /**
     * Sends a subtitle to the specified audience.
     *
     * @param audience     the audience where the subtitle will be sent
     * @param subtitle     the subtitle to be sent
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     */
    public void sendSubtitle(Audience audience, String subtitle, Title.Times times, TagResolver... tagResolvers) {
        sendTitle(audience, null, subtitle, times, tagResolvers);
    }

    /**
     * Sends a title to the specified audience.
     *
     * @param audience     the audience to send the title to
     * @param title        the title to send
     * @param subtitle     the subtitle to send
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     */
    public void sendTitle(Audience audience, String title, String subtitle, TagResolver... tagResolvers) {
        sendTitle(audience, title, subtitle, null, tagResolvers);
    }

    /**
     * Sends a title to the specified audience.
     *
     * @param audience     the audience to send the title to
     * @param title        the title to send
     * @param subtitle     the subtitle to send
     * @param times        the timings of the title
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     */
    public void sendTitle(Audience audience, @Nullable String title, @Nullable String subtitle, @Nullable Title.Times times, TagResolver... tagResolvers) {
        var titleComponent = title != null ? nullable(mapping.apply(audience), title, tagResolvers) : null;
        var subtitleComponent = subtitle != null ? nullable(mapping.apply(audience), subtitle, tagResolvers) : null;
        if (titleComponent != null || subtitleComponent != null) audience.showTitle(Title.title(
                titleComponent != null ? titleComponent : Component.empty(),
                subtitleComponent != null ? subtitleComponent : Component.empty(),
                times
        ));
    }

    /**
     * Sends an action bar message to the specified audience.
     *
     * @param audience     the audience to send the action bar message to
     * @param key          the key to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     */
    public void sendActionBar(Audience audience, String key, TagResolver... tagResolvers) {
        var message = nullable(mapping.apply(audience), key, tagResolvers);
        if (message != null) audience.sendActionBar(message);
    }
}

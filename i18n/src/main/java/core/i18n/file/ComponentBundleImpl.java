package core.i18n.file;

import core.file.Validatable;
import core.file.format.PropertiesFile;
import core.io.IO;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

@NullMarked
class ComponentBundleImpl implements ComponentBundle {
    private static final Logger LOGGER = LoggerFactory.getLogger("i18n");
    private final Locale fallback;
    private final MiniMessageTranslationStore translator;

    private ComponentBundleImpl(Locale fallback, MiniMessageTranslationStore translator) {
        this.fallback = fallback;
        this.translator = translator;
    }

    @Override
    public MiniMessageTranslationStore translator() {
        return translator;
    }

    @Override
    public ComponentBundleImpl registerTranslations() throws IllegalStateException {
        if (GlobalTranslator.translator().addSource(translator)) return this;
        throw new IllegalStateException("Translation store '" + translator.name() + "' already registered");
    }

    @Override
    public void unregisterTranslations() throws IllegalStateException {
        if (GlobalTranslator.translator().removeSource(translator)) return;
        throw new IllegalStateException("Translation store '" + translator.name() + "' not registered");
    }

    @Override
    public @Nullable Component translate(String translationKey, Audience audience, ComponentLike... arguments) {
        return translate(Component.translatable(translationKey).arguments(arguments), audience);
    }

    @Override
    public @Nullable Component translate(String translationKey, Locale locale, ComponentLike... arguments) {
        return translate(Component.translatable(translationKey, arguments), locale);
    }

    @Override
    public @Nullable Component translate(TranslatableComponent component, Audience audience) {
        return translate(component, audience.get(Identity.LOCALE).orElse(fallback));
    }

    @Override
    public @Nullable Component translate(TranslatableComponent component, Locale locale) {
        return translator.translate(component, locale);
    }

    @Override
    public void sendMessage(Audience audience, String translationKey, ComponentLike... arguments) {
        var translated = translate(translationKey, audience, arguments);
        if (translated != null && !Component.empty().equals(translated)) audience.sendMessage(translated);
    }

    @Override
    public void sendMessage(Audience audience, String translationKey, TagResolver resolver) {
        sendMessage(audience, translationKey, Argument.tagResolver(resolver));
    }

    @Override
    public void sendActionBar(Audience audience, String translationKey, ComponentLike... arguments) {
        var translated = translate(translationKey, audience, arguments);
        if (translated != null && !Component.empty().equals(translated)) audience.sendActionBar(translated);
    }

    @Override
    public void showTitle(Audience audience, @Nullable String title, @Nullable String subtitle, Title.@Nullable Times times, ComponentLike... arguments) {
        var titleComponent = title != null ? translate(title, audience, arguments) : null;
        var subtitleComponent = subtitle != null ? translate(subtitle, audience, arguments) : null;
        if (subtitleComponent != null || titleComponent != null) audience.showTitle(Title.title(
                titleComponent != null ? titleComponent : Component.empty(),
                subtitleComponent != null ? subtitleComponent : Component.empty(),
                times
        ));
    }

    @Override
    public void showTitle(Audience audience, @Nullable String title, @Nullable String subtitle, ComponentLike... arguments) {
        showTitle(audience, title, subtitle, Title.DEFAULT_TIMES, arguments);
    }

    public static final class Builder implements ComponentBundle.Builder {
        private final Map<String, Locale> files = new HashMap<>();

        private @Nullable ResourceMigrator migrator = null;
        private Charset charset = StandardCharsets.UTF_8;
        private Locale fallback = Locale.US;
        private MiniMessage miniMessage = MiniMessage.miniMessage();
        private Validatable.Scope scope = Validatable.Scope.FILTER_AND_FILL;

        private Key name;
        private Path path;

        Builder(Key name, Path path) {
            this.name = name;
            this.path = path;
        }

        @Override
        public ComponentBundle.Builder charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        @Override
        public ComponentBundle.Builder fallback(Locale fallback) {
            this.fallback = fallback;
            return this;
        }

        @Override
        public ComponentBundle.Builder migrator(@Nullable ResourceMigrator migrator) {
            this.migrator = migrator;
            return this;
        }

        @Override
        public ComponentBundle.Builder miniMessage(MiniMessage miniMessage) {
            this.miniMessage = miniMessage;
            return this;
        }

        @Override
        public ComponentBundle.Builder name(Key name) {
            this.name = name;
            return this;
        }

        @Override
        public ComponentBundle.Builder path(Path path) {
            this.path = path;
            return this;
        }

        @Override
        public Builder resource(String name, Locale locale) throws IllegalStateException {
            var suffix = ".properties";
            var key = name.endsWith(suffix) ? name : name + suffix;
            if (files.put(key, locale) == null) return this;
            throw new IllegalStateException("Resource '" + key + "' already registered for locale " + locale);
        }

        @Override
        public ComponentBundle.Builder scope(Validatable.Scope scope) {
            this.scope = scope;
            return this;
        }

        @Override
        public ComponentBundle build() throws ResourceMigrationException {
            var registry = MiniMessageTranslationStore.create(name, miniMessage);
            registry.defaultLocale(fallback);
            registerResources(registry);
            return new ComponentBundleImpl(fallback, registry);
        }

        private void registerResources(MiniMessageTranslationStore registry) {
            files.forEach((name, locale) -> {
                try {
                    registry.registerAll(locale, extractResource(name, locale));
                } catch (IOException e) {
                    LOGGER.error("Failed to register resource '{}' ({})", name, locale, e);
                }
            });
        }

        private @Unmodifiable Map<String, String> extractResource(String baseName, Locale locale) throws IOException {
            var file = new PropertiesFile(IO.of(path.resolve(baseName)), charset, readResource(baseName));

            migrate(baseName, locale, file);

            file.validate(scope);

            if (file.getRoot().isEmpty()) file.delete();
            else file.save();

            var properties = new HashMap<String, String>(file.getRoot().size());
            file.getRoot().forEach((key, value) -> properties.put(key.toString(), value.toString()));
            return properties;
        }

        private Properties readResource(String name) throws IOException {
            try (var resource = getClass().getClassLoader().getResourceAsStream(name)) {
                if (resource != null) return readResource(resource);
                throw new IOException("Resource '" + name + "' not found in classpath");
            }
        }

        private Properties readResource(InputStream resource) throws IOException {
            try (var reader = new InputStreamReader(resource, charset);
                 var buffer = new BufferedReader(reader)) {
                var properties = new Properties();
                properties.load(buffer);
                return properties;
            }
        }

        private void migrate(String baseName, Locale locale, PropertiesFile file) throws IOException {
            var oldResource = migrator != null ? migrator.getOldResourceName(locale) : null;

            var oldPath = migrator != null ? migrator.getOldPath() : null;
            if (path.equals(oldPath)) throw new ResourceMigrationException("New and old path cannot match");

            var migrate = (oldPath != null || !baseName.equals(oldResource))
                          && (oldResource != null || oldPath != null);

            if (migrate) migrate(baseName, file, oldPath, oldResource);

            if (migrate || file.getIO().exists()) try {
                migrateResource(baseName, file);
            } catch (Exception e) {
                throw new ResourceMigrationException("An error occurred while migrating resource '" + file.getIO() + "'", e);
            }
        }

        private void migrate(String baseName, PropertiesFile file, @Nullable Path oldPath, @Nullable String oldResource) throws IOException {
            var actualPath = oldPath != null ? oldPath : path;
            var actualResource = oldResource != null ? oldResource : baseName;
            var oldFile = new PropertiesFile(IO.of(actualPath.resolve(actualResource)), charset);
            file.merge(oldFile.getRoot());
            if (!oldFile.delete()) LOGGER.warn("Failed to delete old resource '{}'", oldFile.getIO());
            LOGGER.debug("Migrated resource '{}' to '{}'", oldFile.getIO(), file.getIO());
        }

        private void migrateResource(String resource, PropertiesFile file) {
            if (migrator == null || !migrator.shouldMigrate(resource, file.getRoot())) return;

            var migrated = new Properties(file.getRoot().size());

            file.getRoot().forEach((key, message) -> {
                var migration = migrator.migrate(miniMessage, key.toString(), message.toString());
                if (migration == null) {
                    migrated.put(key, message);
                    return;
                }

                if (migration.drop()) return;

                var migratedKey = migration.key() != null ? migration.key() : key;
                var migratedMessage = migration.message() != null ? migration.message() : message;

                migrated.put(migratedKey, migratedMessage);
            });

            file.setRoot(migrated);
        }
    }
}

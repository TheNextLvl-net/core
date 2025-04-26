package core.i18n.file;

import core.file.Validatable;
import core.file.format.PropertiesFile;
import core.io.IO;
import core.util.Properties;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;

@NullMarked
class ComponentBundleImpl implements ComponentBundle {
    private static final Logger LOGGER = LoggerFactory.getLogger("i18n");
    private final MiniMessage miniMessage;
    private final MiniMessageTranslationStore translator;

    private ComponentBundleImpl(MiniMessage miniMessage, MiniMessageTranslationStore translator) {
        this.miniMessage = miniMessage;
        this.translator = translator;
    }

    @Override
    public MiniMessage miniMessage() {
        return miniMessage;
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

    public static final class Builder implements ComponentBundle.Builder {
        private final Map<String, Locale> files = new HashMap<>();

        private Charset charset = StandardCharsets.UTF_8;
        private Locale fallback = Locale.US;
        private MiniMessage miniMessage = MiniMessage.miniMessage();
        private Validatable.Scope scope = Validatable.Scope.FILTER_AND_FILL;
        private boolean escapeSingleQuotes = false;

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
        public ComponentBundle.Builder escapeSingleQuotes(boolean escape) {
            this.escapeSingleQuotes = escape;
            return this;
        }

        @Override
        public ComponentBundle.Builder fallback(Locale fallback) {
            this.fallback = fallback;
            return this;
        }

        @Override
        public Builder fallback(Locale fallback) {
            this.fallback = fallback;
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
        public Builder resource(String name, Locale locale) {
            var suffix = ".properties";
            files.put(name.endsWith(suffix) ? name : name + suffix, locale);
            return this;
        }

        @Override
        public ComponentBundle build() {
            files.keySet().forEach(this::extractResource);
            var registry = MiniMessageTranslationStore.create(name, miniMessage);
            registry.defaultLocale(fallback);
            files.forEach((path, locale) -> registerBundle(registry, path, locale));
            return new ComponentBundleImpl(miniMessage, registry);
        }

        private void registerBundle(MiniMessageTranslationStore registry, String path, Locale locale) {
            var resolved = this.path.resolve(path);
            if (Files.exists(resolved)) try (var reader = Files.newBufferedReader(resolved, charset)) {
                registry.registerAll(locale, new PropertyResourceBundle(reader), escapeSingleQuotes);
            } catch (IOException e) {
                LOGGER.error("Failed to register resource bundle: {}", path, e);
            }
        }

        private void extractResource(String baseName) {
            try (var io = IO.ofResource(baseName)) {
                var resource = io.isReadable() ? new Properties().read(
                        io.inputStream(StandardOpenOption.READ), charset
                ) : null;
                if (resource == null) throw new FileNotFoundException("Resource not found: " + baseName);
                var file = new PropertiesFile(IO.of(path.resolve(baseName)), charset, resource);
                file.validate(scope).getRoot().merge(resource);
                if (!file.getRoot().isEmpty()) file.save();
            } catch (IOException e) {
                LOGGER.error("Failed to extract resource: {}", baseName, e);
            }
        }
    }
}

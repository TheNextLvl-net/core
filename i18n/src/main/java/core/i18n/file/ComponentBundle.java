package core.i18n.file;

import core.file.Validatable;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jspecify.annotations.NullMarked;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;

/**
 * This class manages internationalization using a {@link MiniMessageTranslationStore},
 * providing a fluent {@link Builder} for resource configuration.
 */
@NullMarked
public interface ComponentBundle {
    /**
     * Retrieves the {@link MiniMessageTranslationStore} associated with this {@link ComponentBundle}.
     *
     * @return the {@link MiniMessageTranslationStore} instance used for managing translations
     */
    MiniMessageTranslationStore translationStore();

    /**
     * Registers the associated {@link #translationStore() MiniMessageTranslationStore} to the {@link GlobalTranslator}.
     *
     * @return the current {@link ComponentBundle} instance
     * @throws IllegalStateException if the translation store is already registered
     */
    ComponentBundle registerTranslations() throws IllegalStateException;

    /**
     * Unregisters the {@link #translationStore() MiniMessageTranslationStore} from the {@link GlobalTranslator}.
     *
     * @throws IllegalStateException if the translation store is not registered in the global translator
     */
    void unregisterTranslations() throws IllegalStateException;

    /**
     * A builder interface for constructing a {@link ComponentBundle}.
     */
    interface Builder {
        /**
         * Specifies the charset used for reading and writing resource bundles.
         * <p>
         * Defaults to {@link StandardCharsets#UTF_8}.
         *
         * @param charset the character set to use
         * @return the builder instance for method chaining
         */
        Builder charset(Charset charset);

        /**
         * Specifies whether single quotes in resource bundles should be escaped.
         * <p>
         * Default to {@code false}.
         *
         * @param escape whether singles quotes should be escaped
         * @return the builder instance for method chaining
         */
        Builder escapeSingleQuotes(boolean escape);

        /**
         * Specifies the fallback {@link Locale} to use when translating.
         * <p>
         * Defaults to {@link Locale#US}.
         *
         * @param fallback the fallback locale to use
         * @return the builder instance for method chaining
         */
        Builder fallback(Locale fallback);

        /**
         * Sets the {@link MiniMessage} instance to use for message parsing and serialization.
         * <p>
         * Defaults to {@link MiniMessage#miniMessage()}.
         *
         * @param miniMessage the {@code MiniMessage} instance to use
         * @return the builder instance for method chaining
         */
        Builder miniMessage(MiniMessage miniMessage);

        /**
         * Sets the name of the {@link ComponentBundle}.
         *
         * @param name the name of the component bundle
         * @return the builder instance for method chaining
         */
        Builder name(Key name);

        /**
         * Sets the base path where resource bundles are saved to and read from.
         *
         * @param path the path to be used for saving and reading resources
         * @return the builder instance for method chaining
         */
        Builder path(Path path);

        /**
         * Adds a resource bundle to the builder with the specified name and locale.
         * <p>
         * The name doesn't have to end with ".properties",
         * it will be appended automatically if omitted.
         *
         * @param name   the name of the resource bundle
         * @param locale the locale associated with the resource bundle
         * @return the builder instance for method chaining
         */
        Builder resource(String name, Locale locale);

        /**
         * Sets the {@link Validatable.Scope} for the builder.
         * <p>
         * Defaults to {@link Validatable.Scope#FILTER_AND_FILL}.
         *
         * @param scope the validation scope to be used
         * @return the builder instance for method chaining
         */
        Builder scope(Validatable.Scope scope);

        /**
         * Builds and returns a new {@link ComponentBundle} with the specified resources.
         * <p>
         * This method saves the registered resource bundles from the resource class path to the defined
         * {@link #path(Path) base path}.
         * If the resource bundles have already been saved to the base path,
         * they will be updated based on the defined validation scope.
         *
         * @return a new {@link ComponentBundle}
         */
        ComponentBundle build();
    }

    /**
     * Creates a new {@link ComponentBundle.Builder} for constructing a {@link ComponentBundle}.
     *
     * @param name the key representing the name of the component bundle
     * @param path the base path where resource bundles are saved to and read from
     * @return a new {@link ComponentBundle.Builder} instance
     */
    static ComponentBundle.Builder builder(Key name, Path path) {
        return new ComponentBundleImpl.Builder(name, path);
    }
}

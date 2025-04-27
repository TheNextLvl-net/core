package core.i18n.file;

import core.file.Validatable;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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
    MiniMessageTranslationStore translator();

    /**
     * Registers the associated {@link #translator() MiniMessageTranslationStore} to the {@link GlobalTranslator}.
     *
     * @return the current {@link ComponentBundle} instance
     * @throws IllegalStateException if the translation store is already registered
     */
    ComponentBundle registerTranslations() throws IllegalStateException;

    /**
     * Unregisters the {@link #translator() MiniMessageTranslationStore} from the {@link GlobalTranslator}.
     *
     * @throws IllegalStateException if the translation store is not registered in the global translator
     */
    void unregisterTranslations() throws IllegalStateException;

    /**
     * Translates a given translation key into a localized {@link Component} for the specified {@link Audience},
     * with optional arguments for formatting arguments within the translation.
     *
     * @param translationKey the translation key representing the entry to be localized
     * @param audience       the {@link Audience} to determine the locale used for the translation
     * @param arguments      optional {@link ComponentLike} arguments used to replace arguments within the translation
     * @return the translated {@link Component}, or {@code null} if the translation could not be resolved
     * @see Argument
     */
    @Nullable
    Component translate(String translationKey, Audience audience, ComponentLike... arguments);

    /**
     * Translates a given translation key into a localized {@link Component}
     * based on the specified {@link Locale} and optional arguments for formatting.
     *
     * @param translationKey the key representing the translation entry to be localized
     * @param locale         the {@link Locale} to use for the translation
     * @param arguments      optional {@link ComponentLike} arguments used for replacing arguments in the translation
     * @return the localized {@link Component}, or {@code null} if the translation could not be resolved
     * @see Argument
     */
    @Nullable
    Component translate(String translationKey, Locale locale, ComponentLike... arguments);

    /**
     * Translates the given {@link TranslatableComponent} into a localized {@link Component} based on the
     * specified {@link Audience}'s locale settings.
     *
     * @param component the {@link TranslatableComponent} to be translated
     * @param audience  the {@link Audience} whose locale will be used for the translation
     * @return the translated {@link Component}, or {@code null} if the translation could not be performed
     * @see Argument
     */
    @Nullable
    Component translate(TranslatableComponent component, Audience audience);

    /**
     * Translates the given {@link TranslatableComponent} for the specified {@link Locale}.
     *
     * @param component the {@link TranslatableComponent} to be translated
     * @param locale    the {@link Locale} to use for the translation
     * @return the translated {@link Component}, or {@code null} if the translation
     * could not be performed
     * @see Argument
     */
    @Nullable
    Component translate(TranslatableComponent component, Locale locale);

    /**
     * Sends a localized message to the specified {@link Audience},
     * based on a translation key and optional arguments for formatting.
     * <p>
     * If no translation for the specified {@code translationKey} exists
     * or the translation is empty, no message will be sent
     *
     * @param audience       the {@link Audience} to which the message will be sent
     * @param translationKey the translation key representing the entry to be localized
     * @param arguments      optional {@link ComponentLike} arguments used for replacing arguments in the translation
     */
    void sendMessage(Audience audience, String translationKey, ComponentLike... arguments);

    /**
     * Sends a localized action bar to the specified {@link Audience},
     * based on a translation key and optional arguments for formatting.
     * <p>
     * If no translation for the specified {@code translationKey} exists
     * or the translation is empty, no action bar will be sent
     *
     * @param audience       the {@link Audience} to which the message will be sent
     * @param translationKey the translation key representing the entry to be localized
     * @param arguments      optional {@link ComponentLike} arguments used for replacing arguments in the translation
     */
    void sendActionBar(Audience audience, String translationKey, ComponentLike... arguments);

    /**
     * Displays a localized title and subtitle to the specified {@link Audience},
     * including title times and optional formatting arguments.
     *
     * @param audience  the {@link Audience} to whom the title will be shown
     * @param title     the title text to display
     * @param subtitle  the subtitle text to display
     * @param times     the {@link Title.Times} defining the timing for the title display
     * @param arguments optional {@link ComponentLike} arguments used to replace placeholders in the title or subtitle
     * @see Audience#showTitle(Title)
     */
    void showTitle(Audience audience, @Nullable String title, @Nullable String subtitle, Title.@Nullable Times times, ComponentLike... arguments);

    /**
     * Displays a localized title and subtitle to the specified {@link Audience},
     * including optional formatting arguments.
     *
     * @param audience  the {@link Audience} to whom the title will be displayed
     * @param title     the title text to display, may be {@code null}
     * @param subtitle  the subtitle text to display, may be {@code null}
     * @param arguments optional {@link ComponentLike} arguments used to replace placeholders
     *                  in the title or subtitle
     * @see #showTitle(Audience, String, String, Title.Times, ComponentLike...)
     * @see Audience#showTitle(Title)
     */
    void showTitle(Audience audience, @Nullable String title, @Nullable String subtitle, ComponentLike... arguments);

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
         * Specifies the fallback {@link Locale} to use when translating.
         * <p>
         * Defaults to {@link Locale#US}.
         *
         * @param fallback the fallback locale to use
         * @return the builder instance for method chaining
         */
        Builder fallback(Locale fallback);

        /**
         * Sets a {@link ResourceMigrator} to transform the message strings in resource bundles during processing.
         * A resource migrator can be used to modify or migrate legacy keys or formats to updated versions.
         * <p>
         * The migrator is only called on existing resource bundles,
         * migrations are performed before the {@link #scope(Validatable.Scope) validation scope}.
         *
         * @param migrator the {@link ResourceMigrator} to apply during resource processing
         * @return the builder instance for method chaining
         */
        Builder migrator(@Nullable ResourceMigrator migrator);

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
         * @throws IllegalStateException thrown if the resource was already registered
         */
        Builder resource(String name, Locale locale) throws IllegalStateException;

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
         * <p>
         * Migrations will be performed according to the defined {@link ResourceMigrator}
         *
         * @return a new {@link ComponentBundle}
         * @throws ResourceMigrationException thrown if something went wrong during migration
         * @see #migrator(ResourceMigrator)
         */
        ComponentBundle build() throws ResourceMigrationException;
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

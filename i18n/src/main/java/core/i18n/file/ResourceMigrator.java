package core.i18n.file;

import core.util.Properties;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.Locale;

/**
 * An interface for performing resource migrations.
 */
public interface ResourceMigrator {
    /**
     * Performs a migration on a given key and message,
     * allowing modification or transformation during resource processing.
     * <p>
     * Returning null indicates that no migration should be performed.
     * If both key and message of the {@link Migration} are null, the entry will be dropped.
     * Returning a changed key or message will migrate that entry to the new values.
     *
     * @param miniMessage the {@link MiniMessage} instance used for message processing
     * @param key         the key associated with the resource to be migrated
     * @param message     the message associated with the resource to be migrated
     * @return a {@link Migration} object containing the migrated key and message,
     * or null if no migration should be performed
     * @see Migration#DROP
     */
    @Nullable
    default Migration migrate(@NonNull MiniMessage miniMessage, @NonNull String key, @NonNull String message) {
        return null;
    }

    // whether any migration in the given resource file should be performed
    // always performs migrations by default
    default boolean shouldMigrate(String resource, Properties properties) {
        return true;
    }

    // this method is called after successful migration of a resource
    default void postMigration(String resource, Properties properties) {
    }

    // if this returns null no base path migration will be performed
    // if not null the old path will be used to look up resources
    // all old resources will be migrated to the path defined in ComponentBundle#Builder
    // this will throw if old and new path are the same
    default @Nullable Path getOldPath() throws ResourceMigrationException {
        return null;
    }

    @Nullable
    // if this returns null no file name migration will be performed
    // if not null and migration succeeded the old file will be deleted
    // this will be done before #migrate
    default String getOldResourceName(@NonNull Locale locale) {
        return null;
    }

    /**
     * Represents the result of a resource migration.<br>
     * A migration contains the transformed key and message after processing.
     * <p>
     * If both key and message are {@code null}, the result signifies
     * the removal of the associated entry, as indicated by {@link #DROP}.
     *
     * @param key     the migrated key, or {@code null} if the key should be discarded
     * @param message the migrated message, or {@code null} if the message should be discarded
     * @see ResourceMigrator#migrate(MiniMessage, String, String)
     * @see Migration#DROP
     */
    record Migration(@Nullable String key, @Nullable String message) {
        /**
         * A constant representing a migration result that indicates the removal of an entry.<br>
         *
         * @see ResourceMigrator#migrate(MiniMessage, String, String)
         */
        public static final @NonNull Migration DROP = new Migration(null, null);

        /**
         * Determines if the migration result signifies the removal of an entry.
         *
         * @return {@code true} if the key and message are both {@code null}
         * @see #DROP
         */
        public boolean drop() {
            return key == null && message == null;
        }
    }
}

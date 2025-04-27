package core.i18n.file;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

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

    /**
     * Determines if a migration should be performed for the specified resource file.
     * <p>
     * Migrations are always performed by default.
     *
     * @param resource   the name of the resource file to be checked
     * @param properties the {@code Properties} file
     * @return {@code true} if the migration should be performed, {@code false} otherwise
     */
    default boolean shouldMigrate(String resource, Properties properties) {
        return true;
    }

    /**
     * Retrieves the old path to be used for resource lookups and migrations.
     * <p>
     * If this method returns {@code null}, no base path migration will be performed.<br>
     * If a non-null value is returned, this path will be used to look old up resources,
     * and all old resources will be migrated to the path specified in
     * {@link ComponentBundle.Builder#path(Path) ComponentBundle.Builder}.
     *
     * @return the old path to be used for resource migration, or {@code null} if no migration should occur
     * @throws ResourceMigrationException thrown if both old and new path are the same
     * @see #getOldResourceName(Locale)
     */
    default @Nullable Path getOldPath() throws ResourceMigrationException {
        return null;
    }

    /**
     * Retrieves the old resource name associated with the provided locale.
     * <p>
     * If this method returns {@code null}, no file name migration will be performed.
     * If a non-null value is returned and the migration is successful, the old file
     * will be deleted before the {@link #migrate(MiniMessage, String, String) migration} process.
     *
     * @param locale the {@link Locale} for which the old resource name is being retrieved
     * @return the old resource name as a {@link String} if migration should occur,
     * or {@code null} if no migration is needed
     * @see #migrate(MiniMessage, String, String)
     * @see #getOldPath()
     */
    @Nullable
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

package core.paper.cache;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Represents a cache for player data.
 *
 * @since 3.0.0
 */
public sealed interface PlayerCache permits SimplePlayerCache {
    /**
     * Creates a new PlayerCache instance with the specified retention duration.
     *
     * @param retention the duration for which player data is retained in cache
     * @return a new PlayerCache instance
     */
    @Contract(value = "_ -> new", pure = true)
    static PlayerCache create(Duration retention) {
        return new SimplePlayerCache(retention);
    }

    /**
     * Returns the duration for which player data is retained in cache.
     *
     * @return the retention duration
     */
    @Contract(pure = true)
    Duration getRetention();

    /**
     * Retrieves a stream of UUIDs representing player data files stored in the "playerdata" directory.
     *
     * @param filter an optional filter function to apply to the stream of UUIDs
     * @return a stream of UUIDs for the stored player data files
     */
    @Contract(mutates = "this")
    Stream<UUID> updateStoredPlayers(@Nullable Function<Stream<UUID>, Stream<UUID>> filter);

    /**
     * Retrieves a stream of UUIDs representing player data files stored in the "playerdata" directory.
     * <p>
     * The returned Optional contains a cached version of the stored players if valid, otherwise empty.
     *
     * @return an optional containing a stream of UUIDs for the stored player data files
     */
    @CheckReturnValue
    @Contract(mutates = "this")
    Optional<Stream<UUID>> getStoredPlayers();

    /**
     * Retrieves a stream of UUIDs representing player data files stored in the "playerdata" directory.
     * <p>
     * This method updates the stored players if required, before returning them.
     *
     * @param filter an optional filter function to apply to the stream of UUIDs
     * @return a stream of UUIDs for the stored player data files
     */
    @CheckReturnValue
    @Contract(mutates = "this")
    Stream<UUID> getOrUpdateStoredPlayers(@Nullable Function<Stream<UUID>, Stream<UUID>> filter);
}

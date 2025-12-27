package core.paper.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import core.paper.brigadier.exceptions.ComponentCommandExceptionType;
import core.paper.cache.PlayerCache;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Contract;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Represents an argument type for parsing and suggesting offline players.
 *
 * @since 3.0.0
 */
public final class OfflinePlayerArgumentType implements CustomArgumentType<OfflinePlayer, String> {
    private static final ComponentCommandExceptionType NO_PLAYER_FOUND = new ComponentCommandExceptionType(
            Component.translatable("argument.entity.notfound.player")
    );

    private final PlayerCache cache;

    private OfflinePlayerArgumentType(PlayerCache cache) {
        this.cache = cache;
    }

    /**
     * Creates a new instance of {@link OfflinePlayerArgumentType}.
     * <p>
     * The suggestions are not cached!
     *
     * @return a new {@link OfflinePlayerArgumentType} instance
     * @apiNote It is highly recommended to use {@link #cached(PlayerCache)} instead.
     * @see #cached(PlayerCache)
     */
    @Contract(value = " -> new", pure = true)
    public static OfflinePlayerArgumentType player() {
        return cached(PlayerCache.create(Duration.ZERO));
    }

    /**
     * Creates a new instance of {@link OfflinePlayerArgumentType}.
     *
     * @param cache the player cache to use for retrieving player data
     * @return a new {@link OfflinePlayerArgumentType} instance
     */
    @Contract(value = "_ -> new", pure = true)
    public static OfflinePlayerArgumentType cached(PlayerCache cache) {
        return new OfflinePlayerArgumentType(cache);
    }

    @Override
    public OfflinePlayer parse(StringReader reader) throws CommandSyntaxException {
        var name = getNativeType().parse(reader);
        var player = Bukkit.getOfflinePlayerIfCached(name);
        if (player != null) return player;
        throw NO_PLAYER_FOUND.createWithContext(reader);
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CompletableFuture.supplyAsync(() -> {
            try (var players = cache.getOrUpdateStoredPlayers(null)
                    .map(Bukkit::getOfflinePlayer)
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .filter(s -> s.toLowerCase().contains(builder.getRemainingLowerCase()))
                    .limit(100)) {
                players.forEach(builder::suggest);
            }
            return builder.build();
        });
    }
}

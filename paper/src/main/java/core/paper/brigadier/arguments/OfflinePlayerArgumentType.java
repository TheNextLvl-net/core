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

    private OfflinePlayerArgumentType() {
    }

    /**
     * Creates a new instance of {@link OfflinePlayerArgumentType}.
     *
     * @return a new {@link OfflinePlayerArgumentType} instance
     */
    @Contract(value = " -> new", pure = true)
    public static OfflinePlayerArgumentType player() {
        return new OfflinePlayerArgumentType();
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
            try (var players = PlayerCache.getOfflinePlayers()
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

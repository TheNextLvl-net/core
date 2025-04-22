package core.paper.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import core.paper.cache.PlayerCache;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@NullMarked
public final class CustomArgumentTypes {
    private static final ComponentCommandExceptionType NO_PLAYER_FOUND = new ComponentCommandExceptionType(
            Component.translatable("argument.entity.notfound.player")
    );

    public static ArgumentType<OfflinePlayer> cachedOfflinePlayer() {
        return new WrappedArgumentType<>(StringArgumentType.word(),
                (reader, name) -> {
                    var player = Bukkit.getOfflinePlayerIfCached(name);
                    if (player != null) return player;
                    throw NO_PLAYER_FOUND.createWithContext(reader);
                }, (context, builder) -> CompletableFuture.supplyAsync(() -> {
            PlayerCache.getOfflinePlayers()
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .filter(s -> s.toLowerCase().contains(builder.getRemainingLowerCase()))
                    .limit(100)
                    .forEach(builder::suggest);
            return builder.build();
        }));
    }

    public static ArgumentType<Player> playerExact() {
        return new WrappedArgumentType<>(StringArgumentType.word(),
                (reader, name) -> {
                    var player = Bukkit.getPlayerExact(name);
                    if (player != null) return player;
                    throw NO_PLAYER_FOUND.createWithContext(reader);
                }, (context, builder) -> CompletableFuture.supplyAsync(() -> {
            Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(s -> s.toLowerCase().contains(builder.getRemainingLowerCase()))
                    .forEach(builder::suggest);
            return builder.build();
        }));
    }

    public static ArgumentType<Player> player() {
        return new WrappedArgumentType<>(StringArgumentType.word(),
                (reader, name) -> {
                    var player = Bukkit.getPlayer(name);
                    if (player != null) return player;
                    throw NO_PLAYER_FOUND.createWithContext(reader);
                }, (context, builder) -> CompletableFuture.supplyAsync(() -> {
            Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(s -> s.toLowerCase().contains(builder.getRemainingLowerCase()))
                    .forEach(builder::suggest);
            return builder.build();
        }));
    }
}

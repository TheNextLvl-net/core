package core.paper.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

class CoreArgumentTypes {
    private static final ComponentCommandExceptionType NO_PLAYER_FOUND = new ComponentCommandExceptionType(
            Component.translatable("argument.entity.notfound.player")
    );

    static final WrappedArgumentType<String, OfflinePlayer> offlinePlayer = new WrappedArgumentType<>(StringArgumentType.word(),
            (reader, name) -> {
                var player = Bukkit.getOfflinePlayerIfCached(name);
                if (player != null) return player;
                throw NO_PLAYER_FOUND.createWithContext(reader);
            }, (context, builder) -> {
        Arrays.stream(Bukkit.getOfflinePlayers())
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .filter(s -> s.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    });

    static final WrappedArgumentType<String, Player> playerExact = new WrappedArgumentType<>(StringArgumentType.word(),
            (reader, name) -> {
                var player = Bukkit.getPlayerExact(name);
                if (player != null) return player;
                throw NO_PLAYER_FOUND.createWithContext(reader);
            }, (context, builder) -> {
        Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(s -> s.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    });

    static final WrappedArgumentType<String, Player> player = new WrappedArgumentType<>(StringArgumentType.word(),
            (reader, name) -> {
                var player = Bukkit.getPlayer(name);
                if (player != null) return player;
                throw NO_PLAYER_FOUND.createWithContext(reader);
            }, (context, builder) -> {
        Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(s -> s.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    });
}

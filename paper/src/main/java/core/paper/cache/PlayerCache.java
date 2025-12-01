package core.paper.cache;

import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.CheckReturnValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class PlayerCache {
    private static final Path playerData = Objects.requireNonNull(
            Bukkit.getWorld(Key.key("minecraft", "overworld"))
    ).getWorldPath().resolve("playerdata");

    /**
     * Retrieves a stream of all offline players stored in the player data directory.
     *
     * @return a stream of OfflinePlayer objects representing all stored players
     * @apiNote The resulting stream must be closed
     */
    @CheckReturnValue
    public static Stream<OfflinePlayer> getOfflinePlayers() {
        return getStoredPlayers().map(Bukkit::getOfflinePlayer);
    }

    /**
     * Retrieves a stream of UUIDs representing player data files stored in the "playerdata" directory.
     *
     * @return a stream of UUIDs for the stored player data files
     * @apiNote The resulting stream must be closed
     */
    @CheckReturnValue
    public static Stream<UUID> getStoredPlayers() {
        try {
            var files = Files.list(playerData);
            return files
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".dat"))
                    .map(name -> name.substring(0, name.length() - 4))
                    .map(s -> {
                        try {
                            return UUID.fromString(s);
                        } catch (IllegalArgumentException ignored) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .onClose(files::close);
        } catch (IOException e) {
            return Stream.empty();
        }
    }
}

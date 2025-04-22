package core.paper.cache;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class PlayerCache {
    private static final File playerData = new File(new File(Bukkit.getWorldContainer(), "world"), "playerdata");

    /**
     * Retrieves a stream of all offline players stored in the player data directory.
     *
     * @return a stream of OfflinePlayer objects representing all stored players
     */
    public static Stream<OfflinePlayer> getOfflinePlayers() {
        return getStoredPlayers().stream().map(Bukkit::getOfflinePlayer);
    }

    /**
     * Retrieves a stream of UUIDs representing player data files stored in the "playerdata" directory.
     *
     * @return a stream of UUIDs for the stored player data files
     */
    private static Set<UUID> getStoredPlayers() {
        var players = new HashSet<UUID>();
        var names = playerData.list((dir, name) -> name.endsWith(".dat"));
        if (names == null) return new HashSet<>();
        for (var name : names) {
            try {
                players.add(UUID.fromString(name.substring(0, name.length() - 4)));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return players;
    }
}

package core.paper.cache;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.stream.Stream;

public class PlayerCache {
    private static final File playerData = new File(new File(Bukkit.getWorldContainer(), "world"), "playerdata");

    /**
     * Retrieves a stream of all offline players stored in the player data directory.
     * The resulting stream must be used within a try-with-resources block to ensure proper resource management.
     *
     * @return a stream of OfflinePlayer objects representing all stored players
     * @throws IOException if an I/O error occurs while accessing the player data
     */
    public static Stream<OfflinePlayer> getOfflinePlayers() throws IOException {
        return getStoredPlayers().map(Bukkit::getOfflinePlayer);
    }

    /**
     * Retrieves a stream of UUIDs representing player data files stored in the "playerdata" directory.
     * The resulting stream must be used within a try-with-resources block to ensure proper resource management.
     *
     * @return a stream of UUIDs for the stored player data files
     * @throws IOException if an I/O error occurs while accessing the directory or files
     */
    @SuppressWarnings("resource")
    private static Stream<UUID> getStoredPlayers() throws IOException {
        return Files.list(playerData.toPath())
                .map(path -> path.getFileName().toString())
                .filter(name -> name.endsWith(".dat"))
                .map(name -> UUID.fromString(name.substring(0, name.length() - 4)));
    }
}

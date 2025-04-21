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

    public static Stream<OfflinePlayer> getOfflinePlayers() {
        return getStoredPlayers().map(Bukkit::getOfflinePlayer);
    }

    private static Stream<UUID> getStoredPlayers() {
        try (var files = Files.list(playerData.toPath())) {
            return files.map(path -> path.getFileName().toString())
                    .filter(name -> name.endsWith(".dat"))
                    .map(name -> UUID.fromString(name.substring(0, name.length() - 4)));
        } catch (IOException e) {
            return Stream.empty();
        }
    }
}

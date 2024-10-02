package core.paper.version;

import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import core.annotation.TypesAreNotNullByDefault;
import core.version.Version;
import core.version.hangar.HangarVersion;
import core.version.hangar.HangarVersionChecker;
import core.version.hangar.Platform;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * The PaperHangarVersionChecker class is an abstract class that provides methods
 * for retrieving and checking the latest supported version of a specific plugin in a Paper Hangar project.
 *
 * @param <V> the type parameter for the version
 */
@TypesAreNotNullByDefault
@FieldsAreNotNullByDefault
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
public abstract class PaperHangarVersionChecker<V extends Version> extends HangarVersionChecker<V> {
    private final @Getter V versionRunning;
    private final @Getter String author;
    private final Plugin plugin;

    @SuppressWarnings("UnstableApiUsage")
    public PaperHangarVersionChecker(Plugin plugin, String author, String slug) {
        super(slug);
        this.versionRunning = Objects.requireNonNull(
                parseVersion(plugin.getPluginMeta().getVersion()),
                "Failed to parse plugin version running"
        );
        this.author = author;
        this.plugin = plugin;
    }

    /**
     * Checks if the plugin is running the latest supported version.
     * This method retrieves the latest supported version asynchronously and compares it with the current version.
     * If the plugin is running the latest version, it logs an informational message.
     * If the plugin is not running the latest version, it logs a warning with a URL for updating.
     * If the plugin is running a snapshot version, it logs a warning indicating that.
     * In case of a failure during the version check, it logs an error message.
     */
    public void checkVersion() {
        retrieveLatestSupportedVersion().thenAccept(version -> {
            var logger = plugin.getComponentLogger();
            if (version.equals(versionRunning)) {
                logger.info("You are running the latest version of {}", plugin.getName());
            } else if (version.compareTo(versionRunning) > 0) {
                logger.warn("An update for {} is available", plugin.getName());
                logger.warn("You are running version {}, the latest supported version is {}", versionRunning, version);
                logger.warn("Update at https://hangar.papermc.io/{}/{}", author, getSlug());
            } else logger.warn("You are running a snapshot version of {}", plugin.getName());
        }).exceptionally(throwable -> {
            plugin.getComponentLogger().error("Version check failed", throwable);
            return null;
        });
    }

    @Override
    public boolean isSupported(HangarVersion version) {
        return version.platformDependencies().get(Platform.PAPER)
                .contains(Bukkit.getMinecraftVersion());
    }
}

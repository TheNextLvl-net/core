package core.paper.version;

import core.version.Version;
import core.version.modrinth.ModrinthVersion;
import core.version.modrinth.ModrinthVersionChecker;
import io.papermc.paper.ServerBuildInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * The PaperModrinthVersionChecker class is an abstract class that provides methods
 * for retrieving and checking the latest supported version of a specific plugin in a Paper Modrinth project.
 *
 * @param <V> the type parameter for the version
 */
@NullMarked
public abstract class PaperModrinthVersionChecker<V extends Version> extends ModrinthVersionChecker<V> implements PluginVersionChecker {
    private final V versionRunning;
    private final Plugin plugin;

    @SuppressWarnings("UnstableApiUsage")
    public PaperModrinthVersionChecker(Plugin plugin, String id) {
        super(id);
        this.plugin = plugin;
        this.versionRunning = Objects.requireNonNull(
                parseVersion(plugin.getPluginMeta().getVersion()),
                "Failed to parse plugin version running"
        );
    }

    @Override
    public void checkVersion() {
        retrieveLatestSupportedVersion().thenAccept(optional -> optional.ifPresentOrElse(this::printVersionInfo,
                () -> retrieveLatestVersion().thenAccept(this::printUnsupportedInfo).exceptionally(throwable -> {
                    plugin.getComponentLogger().warn("There are no public releases for this plugin yet");
                    return null;
                })
        )).exceptionally(throwable -> {
            plugin.getComponentLogger().error("Version check failed", throwable);
            return null;
        });
    }

    @Override
    public void checkLatestVersion() {
        retrieveLatestVersion().thenAccept(this::printVersionInfo).exceptionally(throwable -> {
            plugin.getComponentLogger().warn("There are no public releases for this plugin yet");
            return null;
        });
    }

    private void printUnsupportedInfo(V version) {
        var logger = plugin.getComponentLogger();
        var buildInfo = ServerBuildInfo.buildInfo();
        if (version.equals(versionRunning)) {
            logger.warn("{} seems to be unsupported by {} version {}",
                    buildInfo.minecraftVersionId(), plugin.getName(), versionRunning);
        } else if (version.compareTo(versionRunning) > 0) {
            logger.warn("A new version for {} is available but {} seems to be unsupported",
                    plugin.getName(), buildInfo.minecraftVersionId());
            logger.warn("You are running version {}, the latest version is {}", versionRunning, version);
            logger.warn("Update at https://modrinth.com/project/{}", getId());
            logger.warn("Do not test in production and always make backups before updating");
        } else {
            logger.warn("You are running a snapshot version of {}", plugin.getName());
        }
    }

    private void printVersionInfo(V version) {
        var logger = plugin.getComponentLogger();
        if (version.equals(versionRunning)) {
            logger.info("You are running the latest version of {}", plugin.getName());
        } else if (version.compareTo(versionRunning) > 0) {
            logger.warn("An update for {} is available", plugin.getName());
            logger.warn("You are running version {}, the latest version is {}", versionRunning, version);
            logger.warn("Update at https://modrinth.com/project/{}", getId());
            logger.warn("Do not test in production and always make backups before updating");
        } else logger.warn("You are running a snapshot version of {}", plugin.getName());
    }

    @Override
    public boolean isSupported(ModrinthVersion version) {
        return version.gameVersions().contains(Bukkit.getMinecraftVersion())
               && version.loaders().contains("paper");
    }

    @Override
    public V getVersionRunning() {
        return versionRunning;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}

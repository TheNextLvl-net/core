package core.paper.version;

import core.version.Version;
import core.version.hangar.HangarVersion;
import core.version.hangar.HangarVersionChecker;
import core.version.hangar.Platform;
import io.papermc.paper.ServerBuildInfo;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * The PaperHangarVersionChecker class is an abstract class that provides methods
 * for retrieving and checking the latest supported version of a specific plugin in a Paper Hangar project.
 *
 * @param <V> the type parameter for the version
 */
@Getter
@NullMarked
public abstract class PaperHangarVersionChecker<V extends Version> extends HangarVersionChecker<V> implements PluginVersionChecker {
    private final V versionRunning;
    private final String author;
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
            logger.warn("Update at https://hangar.papermc.io/{}/{}", getAuthor(), getSlug());
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
            logger.warn("Update at https://hangar.papermc.io/{}/{}", getAuthor(), getSlug());
            logger.warn("Do not test in production and always make backups before updating");
        } else logger.warn("You are running a snapshot version of {}", plugin.getName());
    }

    @Override
    public boolean isSupported(HangarVersion version) {
        return version.platformDependencies().get(Platform.PAPER)
                .contains(Bukkit.getMinecraftVersion());
    }
}

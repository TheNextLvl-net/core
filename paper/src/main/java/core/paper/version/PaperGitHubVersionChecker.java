package core.paper.version;

import core.version.Version;
import core.version.github.GitHubVersionChecker;
import core.version.github.Release;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@Getter
@NullMarked
public abstract class PaperGitHubVersionChecker<V extends Version> extends GitHubVersionChecker<V> implements PluginVersionChecker {
    private final V versionRunning;
    private final Plugin plugin;

    @SuppressWarnings("UnstableApiUsage")
    public PaperGitHubVersionChecker(Plugin plugin, String owner, String repository) {
        super(owner, repository);
        this.versionRunning = Objects.requireNonNull(
                parseVersion(plugin.getPluginMeta().getVersion()),
                "Failed to parse plugin version running"
        );
        this.plugin = plugin;
    }

    @Override
    public void checkVersion() {
        retrieveLatestSupportedVersion().thenAccept(version -> {
            var logger = plugin.getComponentLogger();
            if (version.equals(versionRunning)) {
                logger.info("You are running the latest version of {}", plugin.getName());
            } else if (version.compareTo(versionRunning) > 0) {
                logger.warn("An update for {} is available", plugin.getName());
                logger.warn("You are running version {}, the latest supported version is {}", versionRunning, version);
                logger.warn("Update at https://github.com/{}/{}", getOwner(), getRepository());
            } else logger.warn("You are running a snapshot version of {}", plugin.getName());
        }).exceptionally(throwable -> {
            plugin.getComponentLogger().error("Version check failed", throwable);
            return null;
        });
    }

    @Override
    public boolean isSupported(Release version) {
        return true;
    }
}

package core.paper.version;

import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PluginVersionChecker {
    /**
     * Retrieves the plugin associated with this version checker.
     *
     * @return the plugin associated with this checker
     */
    Plugin getPlugin();

    /**
     * Initiates a check for the latest available plugin version.
     * <p>
     * This method attempts to asynchronously retrieve the latest plugin version from the designated source.
     * If the retrieval fails, it logs a warning indicating that no releases are available for the plugin.
     * <p>
     * {@link #checkVersion()} should be preferred over this method in most cases,
     * only use this method if you only want to print information about the latest version,
     * ignoring compatibility.
     *
     * @see #checkVersion() preffered updated checking method
     */
    void checkLatestVersion();

    /**
     * Initiates a check of the plugin version and determines if an update is available.
     * <p>
     * The method retrieves the latest supported version asynchronously
     * and compares it to the currently running version of the plugin.
     * <p>
     * <p>
     * If an update is available,
     * it logs a warning with details about the new version and instructions for obtaining it.
     * <p>
     * If the current version is up to date, a confirmation message is logged.
     * <p>
     * In case no supported version is found,
     * it falls back to checking the latest version
     * and warning the user that their environment might not be supported anymore.
     * <p>
     * In the event of an error during retrieval, an error message is logged.
     *
     * @see #checkLatestVersion()
     */
    void checkVersion();
}

package core.paper.version;

import core.annotation.MethodsReturnNotNullByDefault;
import org.bukkit.plugin.Plugin;

@MethodsReturnNotNullByDefault
public interface PluginVersionChecker {
    /**
     * Retrieves the plugin associated with this version checker.
     *
     * @return the plugin associated with this checker
     */
    Plugin getPlugin();

    /**
     * Checks if the plugin is running the latest supported version.
     * This method retrieves the latest supported version asynchronously and compares it with the current version.
     * If the plugin is running the latest version, it logs an informational message.
     * If the plugin is not running the latest version, it logs a warning with a URL for updating.
     * If the plugin is running a snapshot version, it logs a warning indicating that.
     * In case of a failure during the version check, it logs an error message.
     */
    void checkVersion();
}

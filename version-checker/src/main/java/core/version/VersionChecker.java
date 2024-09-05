package core.version;

import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface VersionChecker<N, V extends Version> {
    /**
     * Retrieves the version of the currently running software.
     *
     * @return the version of the currently running software
     */
    V getVersionRunning();

    /**
     * Parses a version of type N into a version of type V.
     *
     * @param version the version to parse
     * @return the parsed version of type V
     */
    V parseVersion(N version);

    /**
     * Parses a version of type String into a version of type V.
     *
     * @param version the version to parse
     * @return the parsed version of type V
     */
    V parseVersion(String version);

    /**
     * Checks if a version is supported.
     *
     * @param version the version to check
     * @return true if the version is supported, false otherwise
     */
    boolean isSupported(N version);

    /**
     * Asynchronously retrieves the latest available version.
     *
     * @return a CompletableFuture containing the latest version
     */
    CompletableFuture<V> retrieveLatestVersion();

    /**
     * Retrieves all available versions asynchronously.
     *
     * @return a CompletableFuture containing a Set of all versions
     */
    CompletableFuture<@Unmodifiable Set<V>> retrieveVersions();

    /**
     * Retrieves the latest supported version of the software asynchronously.
     *
     * @return a CompletableFuture containing the latest supported version
     */
    CompletableFuture<V> retrieveLatestSupportedVersion();
}

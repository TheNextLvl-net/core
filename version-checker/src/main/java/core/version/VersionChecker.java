package core.version;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

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
     * @return the parsed version of type V, or null if parsing fails
     */
    @Nullable
    V parseVersion(N version);

    /**
     * Parses a version of type String into a version of type V.
     *
     * @param version the version to parse
     * @return the parsed version of type V, or null if parsing fails
     */
    @Nullable
    V parseVersion(String version);

    /**
     * Checks if a version is supported.
     *
     * @param version the version to check
     * @return true if the version is supported, false otherwise
     */
    boolean isSupported(N version);

    /**
     * Retrieves the latest supported version of the software and passes it to the provided success {@link Consumer}.
     *
     * @param success the consumer that will receive the latest supported version wrapped in an {@link Optional}.
     */
    void retrieveLatestSupportedVersion(Consumer<Optional<V>> success);
}

package core.version;

import org.jspecify.annotations.Nullable;

/**
 * The {@code Version} interface represents a version number without format.
 * It provides methods to access the major, minor, and patch components of the version, as well as the pre-release tag.
 * Versions can be compared using the {@link #compareTo(Object)} method.
 */
public interface Version extends Comparable<Version> {
    /**
     * Returns the major component of the version.
     *
     * @return the major component of the version
     */
    int major();

    /**
     * Returns the minor component of the version.
     *
     * @return the minor component of the version
     */
    int minor();

    /**
     * Returns the patch component of the version.
     *
     * @return the patch component of the version
     */
    int patch();

    /**
     * Returns the pre-release tag of the version.
     *
     * @return the pre-release tag of the version, or null if there is no pre-release tag
     */
    @Nullable
    String preRelease();
}

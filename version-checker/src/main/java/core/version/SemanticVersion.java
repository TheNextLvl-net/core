package core.version;

import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The SemanticVersion class represents a <a href="https://semver.org/">semantic version number</a>.
 * It implements the Version interface and provides methods to access and compare version components.
 */
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
public record SemanticVersion(int major, int minor, int patch, @Nullable String preRelease) implements Version {
    /**
     * The PATTERN variable represents the regular expression pattern for a semantic version number.
     * It is used to validate and parse version strings into SemanticVersion objects.
     * The pattern adheres to the <a href="https://semver.org/">Semantic Versioning 2.0.0</a> specification.
     */
    public static final String PATTERN = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$";

    /**
     * Parses a string into a SemanticVersion object.
     *
     * @param string the version string to parse
     * @return the parsed SemanticVersion object, or null if it is not a valid SemVer
     */
    public static @Nullable SemanticVersion parse(String string) {
        try {
            if (!string.matches(PATTERN)) return null;
            var parts = string.split("-", 2);
            var split = parts[0].split("\\.", 3);
            var major = Integer.parseInt(split[0]);
            var minor = Integer.parseInt(split[1]);
            var patch = Integer.parseInt(split[2]);
            return new SemanticVersion(major, minor, patch, parts.length == 2 ? parts[1] : null);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Compares this version with the specified version.
     *
     * @param version the version to be compared
     * @return a negative integer, zero, or a positive integer if this version is less than, equal to, or greater than the specified version
     */
    @Override
    public int compareTo(Version version) {
        return major() != version.major() ? Integer.compare(major(), version.major())
                : minor() != version.minor() ? Integer.compare(minor(), version.minor())
                : patch() != version.patch() ? Integer.compare(patch(), version.patch())
                : Objects.equals(version.preRelease(), preRelease()) ? 0
                : version.preRelease() != null && preRelease() == null ? 1 : -1;
    }

    /**
     * Returns a string representation of the semantic version.
     * The string representation consists of the major, minor, and patch components of the version,
     * separated by periods. If the version has a pre-release tag, it is appended to the string
     * after a hyphen.
     *
     * @return a string representation of the semantic version
     */
    @Override
    public String toString() {
        var string = major() + "." + minor() + "." + patch();
        return preRelease() != null ? string + "-" + preRelease() : string;
    }
}

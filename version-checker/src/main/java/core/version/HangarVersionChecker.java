package core.version;

import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import core.annotation.TypesAreNotNullByDefault;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.mrafonso.hangar4j.HangarClient;
import me.mrafonso.hangar4j.impl.version.HangarVersion;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The HangarVersionChecker class is an abstract class that provides methods
 * for retrieving and checking the latest supported version of a specific component in a hangar project.
 *
 * @param <V> the type parameter for the version
 */
@RequiredArgsConstructor
@TypesAreNotNullByDefault
@FieldsAreNotNullByDefault
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
public abstract class HangarVersionChecker<V extends Version> implements VersionChecker<HangarVersion, V> {
    private final HangarClient hangarClient = new HangarClient(null);
    /**
     * The slug of the project.
     */
    private final @Getter String slug;

    /**
     * Retrieves the latest supported version of a specific component.
     *
     * @param success a consumer function that accepts an {@code Optional} of the latest supported version
     */
    public void retrieveLatestSupportedVersion(Consumer<Optional<V>> success) {
        Objects.requireNonNull(hangarClient.getVersions(getSlug()))
                .thenAcceptAsync(versions -> success.accept(versions.result().stream()
                        .filter(this::isSupported)
                        .map(this::parseVersion)
                        .filter(Objects::nonNull)
                        .max(Version::compareTo)));
    }

    /**
     * Parses a HangarVersion object into a version of type V.
     *
     * @param version the HangarVersion to parse
     * @return the parsed version of type V, or null if parsing fails
     */
    public @Nullable V parseVersion(HangarVersion version) {
        return parseVersion(version.name());
    }
}

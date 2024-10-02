package core.version.hangar;

import com.google.gson.Gson;
import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import core.annotation.TypesAreNotNullByDefault;
import core.version.Version;
import core.version.VersionChecker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Unmodifiable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The HangarVersionChecker class is an abstract class that provides methods
 * for retrieving and checking the latest supported version of a specific component in a hangar project.
 *
 * @param <V> the type parameter for the version
 */
@Getter
@RequiredArgsConstructor
@TypesAreNotNullByDefault
@FieldsAreNotNullByDefault
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
public abstract class HangarVersionChecker<V extends Version> implements VersionChecker<HangarVersion, V> {
    private static final String API_URL = "https://hangar.papermc.io/api/v1/projects/%s/";
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    private static final Gson gson = new Gson();

    /**
     * The slug of the project.
     */
    private final String slug;

    @Override
    public CompletableFuture<V> retrieveLatestVersion() {
        return get("latestrelease")
                .thenApply(HttpResponse::body)
                .thenApply(this::parseVersion);
    }

    @Override
    public CompletableFuture<@Unmodifiable Set<V>> retrieveVersions() {
        return retrieveHangarVersions().thenApply(versions -> versions.stream()
                .map(this::parseVersion)
                .collect(Collectors.toUnmodifiableSet()));
    }

    @Override
    public CompletableFuture<V> retrieveLatestSupportedVersion() {
        return retrieveHangarVersions().thenApply(versions -> versions.stream()
                .filter(this::isSupported)
                .map(this::parseVersion)
                .max(Version::compareTo)
                .orElseThrow());
    }

    @Override
    public V parseVersion(HangarVersion version) {
        return parseVersion(version.name());
    }

    public final CompletableFuture<Set<HangarVersion>> retrieveHangarVersions() {
        return get("versions")
                .thenApply(response -> gson.fromJson(response.body(), HangarVersions.class))
                .thenApply(HangarVersions::result);
    }

    private CompletableFuture<HttpResponse<String>> get(String path) {
        return client.sendAsync(HttpRequest.newBuilder()
                        .uri(URI.create(API_URL.formatted(getSlug()) + path))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
    }
}

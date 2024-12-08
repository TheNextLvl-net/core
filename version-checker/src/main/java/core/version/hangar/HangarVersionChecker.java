package core.version.hangar;

import com.google.gson.Gson;
import core.version.Version;
import core.version.VersionChecker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Optional;
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
@NullMarked
@RequiredArgsConstructor
public abstract class HangarVersionChecker<V extends Version> implements VersionChecker<HangarVersion, V> {
    private static final String API_URL = "https://hangar.papermc.io/api/v1/projects/%s/";
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    private static final Gson gson = new Gson();

    private Set<HangarVersion> versions = new HashSet<>();

    /**
     * The slug of the project.
     */
    private final String slug;

    @Override
    public CompletableFuture<V> retrieveLatestVersion() {
        return get("latestrelease")
                .thenApply(HttpResponse::body)
                .thenCompose(version -> get("versions/" + version))
                .thenApply(response -> {
                    var version = gson.fromJson(response.body(), HangarVersion.class);
                    versions.add(version);
                    return version;
                }).thenApply(this::parseVersion);
    }

    @Override
    public CompletableFuture<Set<V>> retrieveVersions() {
        return retrieveHangarVersions().thenApply(versions -> versions.stream()
                .map(this::parseVersion)
                .collect(Collectors.toUnmodifiableSet()));
    }

    @Override
    public CompletableFuture<Optional<V>> retrieveLatestSupportedVersion() {
        return retrieveHangarVersions().thenApply(versions -> versions.stream()
                .filter(this::isSupported)
                .map(this::parseVersion)
                .max(Version::compareTo));
    }

    @Override
    public Set<V> getSupportedVersions() {
        return versions.stream()
                .filter(this::isSupported)
                .map(this::parseVersion)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<V> getVersions() {
        return versions.stream()
                .map(this::parseVersion)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Optional<V> getLatestSupportedVersion() {
        return versions.stream()
                .filter(this::isSupported)
                .map(this::parseVersion)
                .max(Version::compareTo);
    }

    @Override
    public Optional<V> getLatestVersion() {
        return versions.stream()
                .map(this::parseVersion)
                .max(Version::compareTo);
    }

    @Override
    public V parseVersion(HangarVersion version) {
        return parseVersion(version.name());
    }

    public final CompletableFuture<Set<HangarVersion>> retrieveHangarVersions() {
        return get("versions").thenApply(response -> {
            var versions = gson.fromJson(response.body(), HangarVersions.class);
            this.versions = versions.result();
            return versions;
        }).thenApply(HangarVersions::result);
    }

    private CompletableFuture<HttpResponse<String>> get(String path) {
        return client.sendAsync(HttpRequest.newBuilder()
                        .uri(URI.create(API_URL.formatted(getSlug()) + path))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
    }
}

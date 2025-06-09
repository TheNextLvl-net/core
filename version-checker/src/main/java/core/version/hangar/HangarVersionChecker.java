package core.version.hangar;

import com.google.gson.Gson;
import core.version.Version;
import core.version.VersionChecker;
import org.jspecify.annotations.NullMarked;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Objects;
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
@NullMarked
public abstract class HangarVersionChecker<V extends Version> implements VersionChecker<HangarVersion, V> {
    private static final String API_URL = "https://hangar.papermc.io/api/v1/projects/%s/";
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    private static final Gson gson = new Gson();

    private Set<HangarVersion> versions = new HashSet<>();
    private final String slug;

    /**
     * Constructs a new instance of the HangarVersionChecker with the specified slug.
     *
     * @param slug the unique identifier representing the project or repository.
     */
    public HangarVersionChecker(String slug) {
        this.slug = slug;
    }

    /**
     * Retrieves the slug associated with this project.
     *
     * @return the slug representing the project
     */
    public String getSlug() {
        return slug;
    }

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
            var versions = Objects.requireNonNullElse(gson.fromJson(response.body(), HangarVersions.class), new HangarVersions(this.versions));
            return this.versions = versions.result();
        });
    }

    private CompletableFuture<HttpResponse<String>> get(String path) {
        return client.sendAsync(HttpRequest.newBuilder()
                        .uri(URI.create(API_URL.formatted(getSlug()) + path))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
    }
}

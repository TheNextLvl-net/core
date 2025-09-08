package core.version.modrinth;

import com.google.gson.Gson;
import core.version.Version;
import core.version.VersionChecker;
import org.jspecify.annotations.NullMarked;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The ModrinthVersionChecker class is an abstract class that provides methods
 * for retrieving and checking the latest supported version of a specific component in a modrinth project.
 *
 * @param <V> the type parameter for the version
 */
@NullMarked
public abstract class ModrinthVersionChecker<V extends Version> implements VersionChecker<ModrinthVersion, V> {
    private static final String API_URL = "https://api.modrinth.com/v2/project/%s/";
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    private static final Gson gson = new Gson();

    private Set<ModrinthVersion> versions = new HashSet<>();
    private final String id;

    /**
     * Constructs a new instance of the ModrinthVersionChecker with the specified id.
     *
     * @param id the unique identifier representing the project.
     */
    public ModrinthVersionChecker(String id) {
        this.id = id;
    }

    /**
     * Retrieves the id associated with this project.
     *
     * @return the id representing the project
     */
    public String getId() {
        return id;
    }

    @Override
    public CompletableFuture<V> retrieveLatestVersion() {
        return retrieveModrinthVersions().thenApply(versions -> {
            var modrinthVersions = List.copyOf(versions);
            var version = modrinthVersions.getFirst();
            return version.versionNumber();
        }).thenApply(this::parseVersion);
    }

    @Override
    public CompletableFuture<Set<V>> retrieveVersions() {
        return retrieveModrinthVersions().thenApply(versions -> versions.stream()
                .map(this::parseVersion)
                .collect(Collectors.toUnmodifiableSet()));
    }

    @Override
    public CompletableFuture<Optional<V>> retrieveLatestSupportedVersion() {
        return retrieveModrinthVersions().thenApply(versions -> versions.stream()
                .filter(this::isSupported)
                .map(this::parseVersion)
                .filter(this::isConsidered)
                .max(Version::compareTo));
    }

    @Override
    public Set<V> getSupportedVersions() {
        return versions.stream()
                .filter(this::isSupported)
                .map(this::parseVersion)
                .filter(this::isConsidered)
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
                .filter(this::isConsidered)
                .max(Version::compareTo);
    }

    @Override
    public Optional<V> getLatestVersion() {
        return versions.stream()
                .map(this::parseVersion)
                .max(Version::compareTo);
    }

    @Override
    public V parseVersion(ModrinthVersion version) {
        return parseVersion(version.name());
    }

    public final CompletableFuture<Set<ModrinthVersion>> retrieveModrinthVersions() {
        return get("version").thenApply(response ->
                this.versions = Objects.requireNonNullElse(gson.fromJson(response.body(), ModrinthVersions.class), this.versions));
    }

    private CompletableFuture<HttpResponse<String>> get(String path) {
        return client.sendAsync(HttpRequest.newBuilder()
                        .uri(URI.create(API_URL.formatted(getId()) + path))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
    }
}

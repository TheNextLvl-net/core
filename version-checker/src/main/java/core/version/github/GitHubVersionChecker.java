package core.version.github;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@TypesAreNotNullByDefault
@FieldsAreNotNullByDefault
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
public abstract class GitHubVersionChecker<V extends Version> implements VersionChecker<Release, V> {
    private static final String API_URL = "https://api.github.com/repos/%s/%s/";
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    private static final Gson gson = new Gson();

    /**
     * The owner of the project.
     */
    private final String owner;
    /**
     * The name of the repository.
     */
    private final String repository;

    @Override
    public CompletableFuture<V> retrieveLatestVersion() {
        return get("releases/latest")
                .thenApply(response -> gson.fromJson(response.body(), Release.class))
                .thenApply(release -> parseVersion(release.tag()));
    }

    @Override
    public CompletableFuture<@Unmodifiable Set<V>> retrieveVersions() {
        return retrieveGitHubReleases().thenApply(versions -> versions.stream()
                .map(this::parseVersion)
                .collect(Collectors.toUnmodifiableSet()));
    }

    @Override
    public CompletableFuture<V> retrieveLatestSupportedVersion() {
        return retrieveGitHubReleases().thenApply(versions -> versions.stream()
                .filter(this::isSupported)
                .map(this::parseVersion)
                .max(Version::compareTo)
                .orElseThrow());
    }

    @Override
    public V parseVersion(Release version) {
        return parseVersion(version.tag());
    }

    public final CompletableFuture<Set<Release>> retrieveGitHubReleases() {
        return get("releases").thenApply(response ->
                gson.fromJson(response.body(), new TypeToken<HashSet<Release>>() {
                }));
    }

    private CompletableFuture<HttpResponse<String>> get(String path) {
        return client.sendAsync(HttpRequest.newBuilder()
                        .uri(URI.create(API_URL.formatted(getOwner(), getRepository()) + path))
                        .header("Accept", "application/vnd.github+json")
                        .build(),
                HttpResponse.BodyHandlers.ofString());
    }
}

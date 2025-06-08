package core.version.modrinth;

import com.google.gson.annotations.SerializedName;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

@NullMarked
public record ModrinthVersion(
        @SerializedName("name") String name,
        @SerializedName("version_number") String versionNumber,
        @SerializedName("loaders") Set<Loader> loaders,
        @SerializedName("game_versions") Set<String> gameVersions
) {
}

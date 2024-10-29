package core.version.github;

import com.google.gson.annotations.SerializedName;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record Release(
        @SerializedName("html_url") String url,
        @SerializedName("tag_name") String tag,
        @SerializedName("name") String name,
        @SerializedName("body") String description,
        @SerializedName("draft") boolean draft,
        @SerializedName("prerelease") boolean preRelease
) {
}

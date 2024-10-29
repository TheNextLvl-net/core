package core.version.hangar;

import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Map;

@NullMarked
public record HangarVersion(String name, String author, Map<Platform, List<String>> platformDependencies) {
}

package core.version.hangar;

import java.util.List;
import java.util.Map;

public record HangarVersion(String name, String author, Map<Platform, List<String>> platformDependencies) {
}

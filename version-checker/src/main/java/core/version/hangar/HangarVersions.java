package core.version.hangar;

import org.jspecify.annotations.NullMarked;

import java.util.Set;

@NullMarked
public record HangarVersions(Set<HangarVersion> result) {
}

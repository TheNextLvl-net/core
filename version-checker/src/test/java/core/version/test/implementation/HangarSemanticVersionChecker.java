package core.version.test.implementation;

import core.version.SemanticVersion;
import core.version.hangar.HangarVersion;
import core.version.hangar.HangarVersionChecker;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HangarSemanticVersionChecker extends HangarVersionChecker<SemanticVersion> {
    public HangarSemanticVersionChecker(String slug) {
        super(slug);
    }

    @Override
    public SemanticVersion getVersionRunning() {
        return new SemanticVersion(0, 0, 0, null);
    }

    @Override
    public SemanticVersion parseVersion(String version) {
        return SemanticVersion.parse(version.startsWith("v") ? version.substring(1) : version);
    }

    @Override
    public boolean isSupported(HangarVersion version) {
        return true;
    }
}

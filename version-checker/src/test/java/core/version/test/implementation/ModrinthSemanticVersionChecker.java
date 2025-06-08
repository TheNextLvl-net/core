package core.version.test.implementation;

import core.version.SemanticVersion;
import core.version.modrinth.ModrinthVersion;
import core.version.modrinth.ModrinthVersionChecker;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ModrinthSemanticVersionChecker extends ModrinthVersionChecker<SemanticVersion> {
    public ModrinthSemanticVersionChecker(String id) {
        super(id);
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
    public boolean isSupported(ModrinthVersion version) {
        return true;
    }
}

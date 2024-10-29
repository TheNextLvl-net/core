package core.version.test.implementation;

import core.version.SemanticVersion;
import core.version.github.GitHubVersionChecker;
import core.version.github.Release;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GitHubSemanticVersionChecker extends GitHubVersionChecker<SemanticVersion> {
    public GitHubSemanticVersionChecker(String owner, String repository) {
        super(owner, repository);
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
    public boolean isSupported(Release version) {
        return true;
    }
}

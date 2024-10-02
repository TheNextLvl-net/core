package core.version.test.implementation;

import core.annotation.TypesAreNotNullByDefault;
import core.version.SemanticVersion;
import core.version.github.GitHubVersionChecker;
import core.version.github.Release;

@TypesAreNotNullByDefault
public class GitHubSemanticVersionChecker extends GitHubVersionChecker<SemanticVersion> {
    public GitHubSemanticVersionChecker(String owner, String repository) {
        super(owner, repository);
    }

    @Override
    public SemanticVersion getVersionRunning() {
        return parseVersion(getClass().getPackage().getImplementationVersion());
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

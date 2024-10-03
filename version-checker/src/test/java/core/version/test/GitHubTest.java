package core.version.test;

import core.version.test.implementation.GitHubSemanticVersionChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GitHubTest {
    private static GitHubSemanticVersionChecker versionChecker;

    @BeforeAll
    static void initAll() {
        versionChecker = new GitHubSemanticVersionChecker("TheNextLvl-net", "Arkitektonika");
    }

    @Test
    void testLatestVersion() {
        Assertions.assertNotNull(versionChecker.retrieveLatestVersion().join());
        Assertions.assertTrue(versionChecker.getLatestVersion().isPresent());
    }

    @Test
    void testVersions() {
        Assertions.assertNotNull(versionChecker.retrieveVersions().join());
        Assertions.assertFalse(versionChecker.getVersions().isEmpty());
    }
}

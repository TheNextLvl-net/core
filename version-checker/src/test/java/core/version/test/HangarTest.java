package core.version.test;

import core.version.test.implementation.HangarSemanticVersionChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HangarTest {
    private static HangarSemanticVersionChecker versionChecker;

    @BeforeAll
    static void initAll() {
        versionChecker = new HangarSemanticVersionChecker("Tweaks");
    }

    @Test
    void testLatestVersion() {
        Assertions.assertNotNull(versionChecker.retrieveLatestVersion().join());
    }

    @Test
    void testVersions() {
        Assertions.assertNotNull(versionChecker.retrieveVersions().join());
    }
}

package core.version.test;

import core.version.test.implementation.HangarSemanticVersionChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class HangarTest {
    private static HangarSemanticVersionChecker versionChecker;

    @BeforeAll
    static void initAll() {
        versionChecker = new HangarSemanticVersionChecker("Worlds");
    }

    @Test
    void testLatestVersion() {
        Assertions.assertNotNull(versionChecker.retrieveLatestVersion().orTimeout(10, TimeUnit.SECONDS).join());
        Assertions.assertTrue(versionChecker.getLatestVersion().isPresent());
        System.out.printf("Latest version: %s%n", versionChecker.getLatestVersion().get());
    }

    @Test
    void testVersions() {
        Assertions.assertNotNull(versionChecker.retrieveVersions().orTimeout(10, TimeUnit.SECONDS).join());
        Assertions.assertFalse(versionChecker.getVersions().isEmpty());
        System.out.printf("Versions: %s%n", versionChecker.getVersions());
    }
}

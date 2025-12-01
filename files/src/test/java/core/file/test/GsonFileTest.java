package core.file.test;

import core.file.formats.GsonFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GsonFileTest {
    private static final Path path = Path.of("test.json");

    @Test
    public void createFile() {
        var contents = new Identifier("test", UUID.randomUUID());

        assertFalse(Files.isRegularFile(path), path + " already exists");

        var file = new GsonFile<>(path, contents).saveIfAbsent();

        assertTrue(Files.isRegularFile(path), "Failed to create file");
        assertEquals(contents, file.getRoot(), "File was not saved to disk");

        var modified = new Identifier("lol", UUID.randomUUID());
        file.setRoot(modified);
        file.save();

        assertEquals(modified, new GsonFile<>(path, contents).getRoot(), "File was not overridden");
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(path);
        assertFalse(Files.isRegularFile(path), path + " still exists");
    }

    private record Identifier(String name, UUID uuid) {
    }
}

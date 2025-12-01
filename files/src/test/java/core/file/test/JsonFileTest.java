package core.file.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import core.file.formats.JsonFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonFileTest {
    private static final Path path = Path.of("test.json");

    @Test
    public void createFile() {
        var contents = new JsonObject();
        contents.add("array", new JsonArray());

        assertFalse(Files.isRegularFile(path), path + " already exists");

        var file = new JsonFile<>(path, contents).saveIfAbsent();

        assertTrue(Files.isRegularFile(path), "Failed to create file");
        assertEquals(contents, file.getRoot(), "File was not saved to disk");

        var modified = new JsonObject();
        file.setRoot(modified);
        file.save();

        assertEquals(modified, new JsonFile<>(path, contents).getRoot(), "File was not overridden");
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(path);
        assertFalse(Files.isRegularFile(path), path + " still exists");
    }
}

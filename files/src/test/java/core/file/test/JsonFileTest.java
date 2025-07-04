package core.file.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import core.file.format.JsonFile;
import core.io.IO;
import core.io.PathIO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonFileTest {
    private static final PathIO path = IO.of("test.json");

    @Test
    public void createFile() {
        var contents = new JsonObject();
        contents.add("array", new JsonArray());

        assertFalse(path.exists(), path + " already exists");

        var file = new JsonFile<>(path, contents).saveIfAbsent();

        assertTrue(path.exists(), "Failed to create file");
        assertEquals(contents, file.getRoot(), "File was not saved to disk");

        var modified = new JsonObject();
        file.setRoot(modified);
        file.save();

        assertEquals(modified, new JsonFile<>(path, contents).getRoot(), "File was not overridden");
    }

    @AfterAll
    public static void cleanup() throws IOException {
        path.delete();
        assertFalse(path.exists(), path + " still exists");
    }
}

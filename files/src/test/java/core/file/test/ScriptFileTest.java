package core.file.test;

import core.file.format.ScriptFile;
import core.io.IO;
import core.io.PathIO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScriptFileTest {
    private static final PathIO path = IO.of("test.json");

    @Test
    public void createFile() throws IOException, ExecutionException, InterruptedException {
        var contents = List.of("echo test", "echo lol", "false");

        assertFalse(path.exists(), path + " already exists");

        var file = new ScriptFile(path, contents);
        file.saveIfAbsent();

        assertTrue(path.exists(), "Failed to create file");
        assertEquals(contents, file.getRoot(), "File was not saved to disk");
        
        assertEquals(127, file.run().exitValue(), "Script did not run successfully");

        var modified = List.<String>of();
        file.setRoot(modified);
        file.save();

        assertEquals(modified, new ScriptFile(path, contents).getRoot(), "File was not overridden");
    }

    @AfterAll
    public static void cleanup() throws IOException {
        path.delete();
        assertFalse(path.exists(), path + " still exists");
    }
}

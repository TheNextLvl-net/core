package core.file.test;

import core.file.format.separator.TSVFile;
import core.io.IO;
import core.io.PathIO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TSVFileTest {
    private static final PathIO path = IO.of("test.tsv");

    @Test
    public void createFile() {
        var contents = List.of(
                List.of("test", "test2"),
                List.of("lol", "lol2")
        );

        assertFalse(path.exists(), path + " already exists");

        var file = new TSVFile(path, contents).saveIfAbsent();

        assertTrue(path.exists(), "Failed to create file");
        assertEquals(contents, file.getRoot(), "File was not saved to disk");

        var modified = List.<List<String>>of();
        file.setRoot(modified);
        file.save();

        assertEquals(modified, new TSVFile(path, contents).getRoot(), "File was not overridden");
    }

    @AfterAll
    public static void cleanup() throws IOException {
        path.delete();
        assertFalse(path.exists(), path + " still exists");
    }
}

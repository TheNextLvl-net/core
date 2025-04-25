import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;

@DisplayName("File creation test")
public class FileCreationTest extends BaseTest {
    @Test
    @DisplayName("Non-empty file creation")
    public void nonEmptyFileCreation() {
        Assertions.assertTrue(Files.isRegularFile(OUTPUT.resolve("test.properties")));
        Assertions.assertTrue(Files.isRegularFile(OUTPUT.resolve("test_german.properties")));
        Assertions.assertTrue(Files.isRegularFile(OUTPUT.resolve("test_italian.properties")));
    }

    @Test
    @DisplayName("Empty file creation")
    public void emptyFileCreation() {
        Assertions.assertFalse(Files.isRegularFile(OUTPUT.resolve("test_spanish_empty.properties")));
    }

    @Override
    public @NotNull Key key() {
        return Key.key("test", "file_creation");
    }
}

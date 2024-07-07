import core.i18n.file.ComponentBundle;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Locale;

@DisplayName("File creation test")
public class FileCreationTest {
    private static final File OUTPUT = new File("output");

    @BeforeAll
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void setUp() {
        if (OUTPUT.isDirectory()) OUTPUT.delete();
        new ComponentBundle(OUTPUT, audience -> Locale.US)
                .register("test", Locale.US)
                .register("test_german", Locale.GERMANY)
                .register("test_italian", Locale.ITALY)
                .register("test_spanish_empty", Locale.of("es", "ES"));
    }

    @Test
    @DisplayName("Non-empty file creation")
    public void nonEmptyFileCreation() {
        Assertions.assertTrue(new File(OUTPUT, "test.properties").isFile());
        Assertions.assertTrue(new File(OUTPUT, "test_german.properties").isFile());
        Assertions.assertTrue(new File(OUTPUT, "test_italian.properties").isFile());
    }

    @Test
    @DisplayName("Empty file creation")
    public void emptyFileCreation() {
        Assertions.assertFalse(new File(OUTPUT, "test_spanish_empty.properties").isFile());
    }
}

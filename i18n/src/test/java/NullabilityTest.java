import core.i18n.file.ComponentBundle;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Locale;

public class NullabilityTest {
    private static final File OUTPUT = new File("output");
    private static final Locale SPANISH = Locale.of("es", "ES");
    private static ComponentBundle bundle;

    @BeforeAll
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void setUp() {
        if (OUTPUT.isDirectory()) OUTPUT.delete();
        bundle = new ComponentBundle(OUTPUT, audience -> Locale.US)
                .register("test", Locale.US)
                .register("test_german", Locale.GERMANY)
                .register("test_italian", Locale.ITALY)
                .register("test_spanish", SPANISH);
    }

    @Test
    @DisplayName("not null")
    public void testNotNull() {
        Assertions.assertNotNull(bundle.nullable(Locale.US, "greetings"));
        Assertions.assertNotNull(bundle.nullable(Locale.GERMANY, "greetings"));
        Assertions.assertNotNull(bundle.nullable(Locale.ITALY, "greetings"));
    }

    @Test
    @DisplayName("null")
    public void testNull() {
        Assertions.assertNull(bundle.nullable(SPANISH, "greetings"));
    }
}

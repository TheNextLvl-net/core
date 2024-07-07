import core.i18n.file.ComponentBundle;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Locale;

public class FallbackTest {
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
                .register("test_spanish_empty", SPANISH);
    }

    @Test
    @DisplayName("No fallback")
    public void noFallback() {
        Assertions.assertNotEquals(
                bundle.component(Locale.US, "greetings"),
                bundle.component(Locale.GERMANY, "greetings")
        );
        Assertions.assertNotEquals(
                bundle.component(Locale.US, "greetings"),
                bundle.component(Locale.ITALY, "greetings")
        );
        Assertions.assertNotEquals(
                bundle.component(Locale.GERMANY, "greetings"),
                bundle.component(Locale.ITALY, "greetings")
        );
    }

    @Test
    @DisplayName("Fallback")
    public void fallback() {
        Assertions.assertEquals(
                bundle.component(Locale.US, "greetings"),
                bundle.component(SPANISH, "greetings")
        );
    }
}

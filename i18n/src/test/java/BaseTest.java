import core.i18n.file.ComponentBundle;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;

public abstract class BaseTest implements Keyed {
    protected static final Logger LOGGER = LoggerFactory.getLogger("test");
    protected static final Locale SPANISH = Locale.of("es", "ES");
    protected static final Path OUTPUT = Path.of("output");

    protected ComponentBundle bundle = ComponentBundle.builder(key(), OUTPUT)
            .resource("test.properties", Locale.US)
            .resource("test_german.properties", Locale.GERMANY)
            .resource("test_italian.properties", Locale.ITALY)
            .resource("test_spanish.properties", SPANISH)
            .resource("test_spanish_empty.properties", SPANISH)
            .build();

    @BeforeAll
    public static void setUp() throws IOException {
        Files.createDirectories(OUTPUT);
        try (var stream = Files.walk(OUTPUT)) {
            stream.filter(Files::isRegularFile).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    LOGGER.error("Failed to delete file {}", path, e);
                }
            });
        }
    }

    @Test
    @DisplayName("Global translator un/registration")
    public void testSources() {
        bundle.registerTranslations();

        var translator = GlobalTranslator.translator();
        var translators = new HashSet<Translator>();
        translator.sources().iterator().forEachRemaining(translators::add);
        Assertions.assertTrue(translators.contains(bundle.translationStore()), "Translation store not registered");

        bundle.unregisterTranslations();

        translators.clear();
        translator.sources().iterator().forEachRemaining(translators::add);
        Assertions.assertFalse(translators.contains(bundle.translationStore()), "Translation store not unregistered");
    }
}

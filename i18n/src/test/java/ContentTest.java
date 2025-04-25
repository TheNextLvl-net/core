import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Locale;
import java.util.stream.Stream;

public class ContentTest extends BaseTest {
    @ParameterizedTest
    @MethodSource("content")
    @DisplayName("Content as expected")
    public void testContent(String expected, Locale locale) {
        var translation = bundle.translationStore().translate(Component.translatable("greetings"), locale);
        Assertions.assertEquals(Component.text(expected), translation);
    }

    @Override
    public @NotNull Key key() {
        return Key.key("test", "content");
    }

    public static Stream<Arguments> content() {
        return Stream.of(
                Arguments.argumentSet("english", "Hello", Locale.US),
                Arguments.argumentSet("german", "Hallo", Locale.GERMANY),
                Arguments.argumentSet("italian", "Ciao", Locale.ITALY),
                Arguments.argumentSet("spanish", "", SPANISH)
        );
    }
}

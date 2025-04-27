import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Locale;
import java.util.stream.Stream;

public class ContentTest extends BaseTest {
    @BeforeAll
    public static void setup() {
        cleanup("test.properties", "test_german.properties", "test_italian.properties", "test_spanish.properties");
    }

    @ParameterizedTest
    @MethodSource("content")
    @DisplayName("Content as expected")
    public void testContent(String expected, Locale locale) {
        var translation = bundle.translate("greetings", locale);
        Assertions.assertEquals(Component.text(expected), translation);
    }

    @ParameterizedTest
    @MethodSource("resolvedContent")
    @DisplayName("Resolved content as expected")
    public void testResolvedContent(String expected, ComponentLike argument, Locale locale) {
        var translation = bundle.translate("resolved", locale, argument);
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

    public static Stream<Arguments> resolvedContent() {
        return Stream.of(
                Arguments.argumentSet("english", "Hello World!", Argument.numeric("world", "World"), Locale.US),
                Arguments.argumentSet("german", "Hallo Welt!", Argument.numeric("world", "Welt"), Locale.GERMANY),
                Arguments.argumentSet("italian", "Ciao Mondo!", Argument.numeric("world", "Mondo"), Locale.ITALY),
                Arguments.argumentSet("spanish", "!", Argument.numeric("world", ""), SPANISH),
                Arguments.argumentSet("english", "Hello World!", Argument.tagResolver(TagResolver.resolver("world", Tag.inserting(Component.text("World")))), Locale.US),
                Arguments.argumentSet("german", "Hallo Welt!", Argument.tagResolver(TagResolver.resolver("world", Tag.inserting(Component.text("Welt")))), Locale.GERMANY),
                Arguments.argumentSet("italian", "Ciao Mondo!", Argument.tagResolver(TagResolver.resolver("world", Tag.inserting(Component.text("Mondo")))), Locale.ITALY),
                Arguments.argumentSet("spanish", "!", Argument.tagResolver(TagResolver.resolver("world", Tag.inserting(Component.empty()))), SPANISH)
        );
    }
}

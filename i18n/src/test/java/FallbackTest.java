import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class FallbackTest extends BaseTest {
    @Test
    @DisplayName("No default locale fallback")
    public void noFallback() {
        Assertions.assertNotEquals(
                bundle.translationStore().translate(Component.translatable("greetings"), Locale.US),
                bundle.translationStore().translate(Component.translatable("greetings"), Locale.GERMANY)
        );
        Assertions.assertNotEquals(
                bundle.translationStore().translate(Component.translatable("greetings"), Locale.US),
                bundle.translationStore().translate(Component.translatable("greetings"), Locale.ITALY)
        );
        Assertions.assertNotEquals(
                bundle.translationStore().translate(Component.translatable("greetings"), Locale.GERMANY),
                bundle.translationStore().translate(Component.translatable("greetings"), Locale.ITALY)
        );
    }

    @Test
    @DisplayName("Fallback to default locale")
    public void fallback() {
        Assertions.assertEquals(
                bundle.translationStore().translate(Component.translatable("hello"), Locale.US),
                bundle.translationStore().translate(Component.translatable("hello"), SPANISH)
        );
    }

    @Override
    public @NotNull Key key() {
        return Key.key("test", "fallback");
    }
}

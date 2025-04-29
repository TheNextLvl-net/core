import net.kyori.adventure.key.Key;
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
                bundle.translate("greetings", Locale.US),
                bundle.translate("greetings", Locale.GERMANY)
        );
        Assertions.assertNotEquals(
                bundle.translate("greetings", Locale.US),
                bundle.translate("greetings", Locale.ITALY)
        );
        Assertions.assertNotEquals(
                bundle.translate("greetings", Locale.GERMANY),
                bundle.translate("greetings", Locale.ITALY)
        );
    }

    @Test
    @DisplayName("Fallback to default locale")
    public void fallback() {
        Assertions.assertEquals(
                bundle.translate("success", Locale.US),
                bundle.translate("success", SPANISH)
        );
    }

    @Override
    public @NotNull Key key() {
        return Key.key("test", "fallback");
    }
}

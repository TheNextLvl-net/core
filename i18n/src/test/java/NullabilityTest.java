import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class NullabilityTest extends BaseTest {
    @Test
    @DisplayName("not null")
    public void testNotNull() {
        Assertions.assertTrue(bundle.translator().contains("greetings", Locale.US));
        Assertions.assertTrue(bundle.translator().contains("greetings", Locale.GERMANY));
        Assertions.assertTrue(bundle.translator().contains("greetings", Locale.ITALY));
    }

    @Test
    @DisplayName("null")
    public void testNull() {
        Assertions.assertNull(bundle.translator().translate("greetings", SPANISH));
        Assertions.assertNull(bundle.translator().translate("unknown", SPANISH));
    }

    @Override
    public @NotNull Key key() {
        return Key.key("test", "nullability");
    }
}

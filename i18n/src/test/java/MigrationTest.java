import core.i18n.file.ComponentBundle;
import core.i18n.file.ResourceMigrator;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

public class MigrationTest extends BaseTest implements ResourceMigrator {
    private final Map<String, String> keys = Map.of(
            "old_success", "success"
    );
    private final Map<Locale, String> oldFiles = Map.of(
            Locale.US, "outdated.properties",
            Locale.GERMANY, "outdated_german.properties"
    );

    @Test
    @DisplayName("Migrate old keys and messages")
    public void testStringMigration() {
        var oldResources = ComponentBundle.builder(key(), OUTPUT);
        oldFiles.forEach((locale, file) -> oldResources.resource(file, locale));
        oldResources.build();

        var bundle = ComponentBundle.builder(key(), OUTPUT)
                .resource("up_to_date.properties", Locale.US)
                .resource("up_to_date_german.properties", Locale.GERMANY)
                .migrator(this)
                .build();

        var english = bundle.translator().translate(Component.translatable("success"), Locale.US);
        Assertions.assertEquals(Component.text("migrated"), english);
        var german = bundle.translator().translate(Component.translatable("success"), Locale.GERMANY);
        Assertions.assertEquals(Component.text("Migriert"), german);
    }

    @Override
    public @NotNull Key key() {
        return Key.key("test", "migration");
    }

    @Override
    public @Nullable Migration migrate(@NonNull MiniMessage miniMessage, @NonNull String key, @NonNull String message) {
        return new Migration(keys.getOrDefault(key, key), null);
    }

    @Override
    public @Nullable String getOldResourceName(@NonNull Locale locale) {
        return oldFiles.get(locale);
    }

    @BeforeAll
    public static void setUp() {
        cleanup("up_to_date.properties", "up_to_date_german.properties");
    }
}

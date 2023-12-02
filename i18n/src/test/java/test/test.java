package test;

import core.i18n.file.ComponentBundle;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Locale;

public class test {
    private static final File directory = new File("lang");

    public static void main(String[] args) {
        var bundle = new ComponentBundle(directory, audience -> audience instanceof Player player
                ? player.locale() : Locale.US)
                .register("test", Locale.US)
                .register("test_german", Locale.GERMANY)
                .register("test_italian", Locale.ITALY);
        System.out.println(bundle.component(Locale.US, "hello"));
        System.out.println(bundle.component(Locale.GERMANY, "hello"));
        System.out.println(bundle.component(Locale.ITALY, "hello"));
    }
}

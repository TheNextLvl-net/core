package test;

import core.i18n.file.ComponentBundle;
import org.bukkit.entity.Player;

import java.util.Locale;

public class test {
    public static void main(String[] args) {
        var register = new ComponentBundle(audience -> audience instanceof Player player
                ? player.locale() : Locale.US)
                .register("test", Locale.US)
                .register("test_german", Locale.GERMANY)
                .register("test_italian", Locale.ITALY)
                .fallback(Locale.US);
        System.out.println(register.component(Locale.ITALY, "hello"));
        System.out.println(register.component(Locale.US, "hello"));
        System.out.println(register.component(Locale.GERMANY, "hello"));
    }
}

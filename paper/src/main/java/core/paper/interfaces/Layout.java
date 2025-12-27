package core.paper.interfaces;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;

sealed public interface Layout permits SimpleLayout {
    @Unmodifiable
    @Contract(value = " -> new", pure = true)
    Map<Character, Renderer> masks();
    
    @Nullable
    @Contract(pure = true)
    Renderer renderer(char c);
    
    void forEachMask(BiConsumer<Character, Renderer> action);

    @Contract(pure = true)
    boolean containsMask(char c);

    @Contract(pure = true)
    boolean hasMasks();

    @Contract(pure = true)
    String pattern();

    @Contract(value = " -> new", pure = true)
    Builder toBuilder();

    @Contract(value = " -> new", pure = true)
    static Layout empty() {
        return new SimpleLayout();
    }

    @Contract(value = "_ -> new", pure = true)
    static Layout of(String... pattern) {
        return new SimpleLayout(pattern);
    }

    static Builder builder() {
        return new SimpleLayout.Builder();
    }

    sealed interface Builder permits SimpleLayout.Builder {
        @Contract(value = "_ -> this", pure = true)
        Builder pattern(String... pattern);

        @Contract(value = "_, _ -> this", pure = true)
        Builder mask(char c, ItemStack item);

        @Contract(value = "_, _ -> this", pure = true)
        Builder mask(char c, Renderer renderer);

        @Contract(value = " -> new", pure = true)
        Layout build();
    }
}

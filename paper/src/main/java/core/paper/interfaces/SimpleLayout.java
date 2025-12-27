package core.paper.interfaces;


import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

final class SimpleLayout implements Layout {
    private final String pattern;
    private final Map<Character, Renderer> items;

    private SimpleLayout(String pattern, Map<Character, Renderer> items) {
        this.pattern = pattern;
        this.items = Map.copyOf(items);
    }

    public SimpleLayout(String... pattern) {
        this.pattern = String.join("\n", pattern);
        this.items = Map.of();
    }

    public SimpleLayout() {
        this.pattern = "";
        this.items = Map.of();
    }

    @Override
    public Map<Character, Renderer> masks() {
        return Map.copyOf(items);
    }

    @Override
    public @Nullable Renderer renderer(char c) {
        return items.get(c);
    }

    @Override
    public void forEachMask(BiConsumer<Character, Renderer> action) {
        items.forEach(action);
    }

    @Override
    public boolean containsMask(char c) {
        return items.containsKey(c);
    }

    @Override
    public boolean hasMasks() {
        return !items.isEmpty();
    }

    @Override
    public String pattern() {
        return pattern;
    }

    @Override
    public Layout.Builder toBuilder() {
        return new Builder();
    }

    static final class Builder implements Layout.Builder {
        private String pattern = "";
        private final Map<Character, Renderer> items = new HashMap<>();

        @Override
        public Layout.Builder pattern(String... pattern) {
            this.pattern = String.join("\n", pattern);
            return this;
        }

        @Override
        public Layout.Builder mask(char c, ItemStack item) {
            this.items.put(c, context -> item);
            return this;
        }

        @Override
        public Layout.Builder mask(char c, Renderer renderer) {
            this.items.put(c, renderer);
            return this;
        }

        @Override
        public Layout build() {
            return new SimpleLayout(pattern, items);
        }
    }
}

package core.api.placeholder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Placeholder<I> {
    public static <I> Placeholder<I> of(String key, @Nullable Object value) {
        return of(key, input -> value);
    }

    public static <I> Placeholder<I> of(String key, Supplier<Object> supplier) {
        return new Placeholder<>() {
            @Override
            public String key() {
                return key;
            }

            @Override
            public String value(@Nullable I input) {
                return String.valueOf(supplier.get());
            }
        };
    }

    public static <I> Placeholder<I> of(String key, Function<I, Object> function) {
        return new Placeholder<>() {
            @Override
            public String key() {
                return key;
            }

            @Override
            public String value(@Nullable I input) {
                return String.valueOf(function.apply(input));
            }
        };
    }

    public abstract String key();

    public abstract String value(@Nullable I input);

    public String value() {
        return value(null);
    }

    @Getter
    @Accessors(fluent = true)
    public static class Registry<I> {
        private final HashMap<String, Placeholder<? extends I>> placeholders = new HashMap<>();

        public boolean isRegistered(String placeholder) {
            return placeholders().containsKey(placeholder);
        }

        public boolean isRegistered(Placeholder<? extends I> placeholder) {
            return isRegistered(placeholder.key());
        }

        public void register(Placeholder<? extends I> placeholder) {
            placeholders().put(placeholder.key(), placeholder);
        }

        public void unregister(String placeholder) {
            placeholders().remove(placeholder);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true)
    public static class Formatter<I> {
        public static final Formatter<Void> DEFAULT = new Formatter<>() {{
            registry().register(Placeholder.of("time", () ->
                    new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())));
            registry().register(Placeholder.of("thread", () -> {
                var name = Thread.currentThread().getName();
                if (name.length() <= 20) return name;
                return name.substring(0, 20) + "...";
            }));
        }};
        private Registry<? extends I> registry = new Registry<>();

        public String format(String text, Collection<? extends Placeholder<? extends I>> collection) {
            return format(text, null, collection);
        }

        public String format(String text, @Nullable I input, Collection<? extends Placeholder<? extends I>> collection) {
            List<Placeholder<? extends I>> placeholders = new ArrayList<>(collection);
            placeholders.addAll(registry.placeholders().values());
            for (var placeholder : placeholders) {
                var value = input == null ? placeholder.value() : ((Placeholder<I>) placeholder).value(input);
                text = text.replace("%" + placeholder.key() + "%", value);
            }
            return equals(DEFAULT) ? text : DEFAULT.format(text);
        }

        @SafeVarargs
        public final String format(String text, Placeholder<? extends I>... placeholders) {
            return format(text, null, placeholders);
        }

        @SafeVarargs
        public final String format(String text, @Nullable I input, Placeholder<? extends I>... placeholders) {
            return format(text, input, Arrays.asList(placeholders));
        }
    }
}

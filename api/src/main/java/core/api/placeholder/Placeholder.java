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

@Deprecated(forRemoval = true, since = "3.1.14")
public abstract class Placeholder<I> {
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static <I> Placeholder<I> of(String key, @Nullable Object value) {
        return of(key, input -> value);
    }

    @Deprecated(forRemoval = true, since = "3.1.14")
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

    @Deprecated(forRemoval = true, since = "3.1.14")
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

    @Deprecated(forRemoval = true, since = "3.1.14")
    public abstract String key();

    @Deprecated(forRemoval = true, since = "3.1.14")
    public abstract String value(@Nullable I input);

    @Deprecated(forRemoval = true, since = "3.1.14")
    public String value() {
        return value(null);
    }

    @Getter
    @Accessors(fluent = true)
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static class Registry<I> {
        @Deprecated(forRemoval = true, since = "3.1.14")
        private final HashMap<String, Placeholder<? extends I>> placeholders = new HashMap<>();

        @Deprecated(forRemoval = true, since = "3.1.14")
        public boolean isRegistered(String placeholder) {
            return placeholders().containsKey(placeholder);
        }

        @Deprecated(forRemoval = true, since = "3.1.14")
        public boolean isRegistered(Placeholder<? extends I> placeholder) {
            return isRegistered(placeholder.key());
        }

        @Deprecated(forRemoval = true, since = "3.1.14")
        public void register(Placeholder<? extends I> placeholder) {
            placeholders().put(placeholder.key(), placeholder);
        }

        @Deprecated(forRemoval = true, since = "3.1.14")
        public void unregister(String placeholder) {
            placeholders().remove(placeholder);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true)
    @Deprecated(forRemoval = true, since = "3.1.14")
    public static class Formatter<I> {
        @Deprecated(forRemoval = true, since = "3.1.14")
        public static final Formatter<Void> DEFAULT = new Formatter<>() {{
            registry().register(Placeholder.of("time", () ->
                    new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())));
            registry().register(Placeholder.of("thread", () -> {
                var name = Thread.currentThread().getName();
                if (name.length() <= 20) return name;
                return name.substring(0, 20) + "...";
            }));
        }};
        @Deprecated(forRemoval = true, since = "3.1.14")
        private Registry<? extends I> registry = new Registry<>();

        @Deprecated(forRemoval = true, since = "3.1.14")
        public String format(String text, Collection<? extends Placeholder<? extends I>> collection) {
            return format(text, null, collection);
        }

        @Deprecated(forRemoval = true, since = "3.1.14")
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
        @Deprecated(forRemoval = true, since = "3.1.14")
        public final String format(String text, Placeholder<? extends I>... placeholders) {
            return format(text, null, placeholders);
        }

        @SafeVarargs
        @Deprecated(forRemoval = true, since = "3.1.14")
        public final String format(String text, @Nullable I input, Placeholder<? extends I>... placeholders) {
            return format(text, input, Arrays.asList(placeholders));
        }
    }
}

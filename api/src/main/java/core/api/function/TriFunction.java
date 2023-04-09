package core.api.function;

import core.annotation.MethodsReturnNonnullByDefault;
import core.annotation.ParametersAreNonnullByDefault;

import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);

    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    default <W> TriFunction<T, U, V, W> andThen(Function<? super R, ? extends W> after) {
        return (t, u, v) -> after.apply(apply(t, u, v));
    }
}

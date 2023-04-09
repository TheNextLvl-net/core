package core.api.function;

import core.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@FunctionalInterface
@MethodsReturnNonnullByDefault
public interface TriPredicate<T, U, V> {
    boolean test(T t, U u, V v);

    @ParametersAreNonnullByDefault
    default TriPredicate<T, U, V> and(TriPredicate<? super T, ? super U, ? super V> other) {
        return (t, u, v) -> this.test(t, u, v) && other.test(t, u, v);
    }

    default TriPredicate<T, U, V> negate() {
        return (t, u, v) -> !this.test(t, u, v);
    }

    @ParametersAreNonnullByDefault
    default TriPredicate<T, U, V> or(TriPredicate<? super T, ? super U, ? super V> other) {
        return (t, u, v) -> this.test(t, u, v) || other.test(t, u, v);
    }
}

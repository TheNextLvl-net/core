package core.function;

import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;

@FunctionalInterface
@MethodsReturnNotNullByDefault
public interface TriPredicate<T, U, V> {
    boolean test(T t, U u, V v);

    @ParametersAreNotNullByDefault
    default TriPredicate<T, U, V> and(TriPredicate<? super T, ? super U, ? super V> other) {
        return (t, u, v) -> this.test(t, u, v) && other.test(t, u, v);
    }

    default TriPredicate<T, U, V> negate() {
        return (t, u, v) -> !this.test(t, u, v);
    }

    @ParametersAreNotNullByDefault
    default TriPredicate<T, U, V> or(TriPredicate<? super T, ? super U, ? super V> other) {
        return (t, u, v) -> this.test(t, u, v) || other.test(t, u, v);
    }
}

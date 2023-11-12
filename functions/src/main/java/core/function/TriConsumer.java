package core.function;

import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;

@FunctionalInterface
@MethodsReturnNotNullByDefault
public interface TriConsumer<T, U, V> {
    void accept(T t, U u, V v);

    @ParametersAreNotNullByDefault
    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
        return (t, u, v) -> {
            this.accept(t, u, v);
            after.accept(t, u, v);
        };
    }
}

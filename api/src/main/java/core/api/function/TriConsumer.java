package core.api.function;

import core.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@FunctionalInterface
@MethodsReturnNonnullByDefault
public interface TriConsumer<T, U, V> {
    void accept(T t, U u, V v);

    @ParametersAreNonnullByDefault
    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
        return (t, u, v) -> {
            this.accept(t, u, v);
            after.accept(t, u, v);
        };
    }
}

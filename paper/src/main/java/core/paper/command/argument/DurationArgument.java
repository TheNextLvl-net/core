package core.paper.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.util.Tick;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

/**
 * A custom argument type implementation to handle durations in a converted format.
 */
@NullMarked
public final class DurationArgument implements CustomArgumentType.Converted<Duration, Integer> {
    private final Duration minimum;

    private DurationArgument(Duration minimum) {
        this.minimum = minimum;
    }

    /**
     * Creates a {@link DurationArgument} instance with a default minimum duration of zero.
     *
     * @return a new {@link DurationArgument} instance with a minimum duration of zero
     */
    @Contract(value = " -> new", pure = true)
    public static DurationArgument duration() {
        return duration(Duration.ZERO);
    }

    /**
     * Creates a new {@link DurationArgument} instance with the specified minimum duration.
     *
     * @param minimum the minimum duration for the argument
     * @return a new {@link DurationArgument} instance with the specified minimum duration
     */
    @Contract(value = "_ -> new", pure = true)
    public static DurationArgument duration(Duration minimum) {
        return new DurationArgument(minimum);
    }

    @Override
    public Duration convert(Integer nativeType) {
        return Tick.of(nativeType);
    }

    @Override
    public ArgumentType<Integer> getNativeType() {
        return ArgumentTypes.time(Tick.tick().fromDuration(minimum));
    }
}

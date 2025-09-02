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
public final class DurationArgumentType implements CustomArgumentType.Converted<Duration, Integer> {
    private final Duration minimum;

    private DurationArgumentType(Duration minimum) {
        this.minimum = minimum;
    }

    /**
     * Creates a {@link DurationArgumentType} instance with a default minimum duration of zero.
     *
     * @return a new {@link DurationArgumentType} instance with a minimum duration of zero
     */
    @Contract(value = " -> new", pure = true)
    public static DurationArgumentType duration() {
        return duration(Duration.ZERO);
    }

    /**
     * Creates a new {@link DurationArgumentType} instance with the specified minimum duration.
     *
     * @param minimum the minimum duration for the argument
     * @return a new {@link DurationArgumentType} instance with the specified minimum duration
     */
    @Contract(value = "_ -> new", pure = true)
    public static DurationArgumentType duration(Duration minimum) {
        return new DurationArgumentType(minimum);
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

package core.paper.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a functional interface for converting a value of type T to a value of type R.
 *
 * @param <T> the type of the input value
 * @param <R> the type of the converted value
 * @deprecated use {@link CustomArgumentType.Converted#convert(Object)}
 */
@NullMarked
@FunctionalInterface
@Deprecated(forRemoval = true, since = "2.3.0")
public interface ResultConverter<T, R> {
    /**
     * Converts a value of type T to a value of type R.
     *
     * @param reader the string reader
     * @param type   the value to be converted
     * @return the converted value
     * @throws CommandSyntaxException if there is an error during conversion
     */
    R convert(StringReader reader, T type) throws CommandSyntaxException;
}

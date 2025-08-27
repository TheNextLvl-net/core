package core.paper.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import core.paper.command.ComponentCommandExceptionType;
import core.paper.command.argument.codec.EnumStringCodec;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * Represents an argument type for parsing and suggesting enum constants.
 * This class allows for conversion between string representations and enum constants of a specified type,
 * enabling the creation of command arguments based on enums.
 *
 * @param <E> the type of the enum
 * @since 2.3.0
 */
@NullMarked
public final class EnumArgumentType<E extends Enum<E>> implements CustomArgumentType<E, String> {
    private final Class<E> enumClass;
    private final EnumStringCodec codec;

    private EnumArgumentType(Class<E> enumClass, EnumStringCodec codec) {
        this.enumClass = enumClass;
        this.codec = codec;
    }

    /**
     * Creates a new instance of {@link EnumArgumentType} for the specified enum class and string codec.
     *
     * @param <E>       the type of the enum
     * @param enumClass the class of the enum type for which the argument type is being created
     * @param codec     the {@link EnumStringCodec} responsible for converting between strings and enum constants
     * @return a new {@link EnumArgumentType} instance
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static <E extends Enum<E>> EnumArgumentType<E> of(Class<E> enumClass, EnumStringCodec codec) {
        return new EnumArgumentType<>(enumClass, codec);
    }

    @Override
    public E parse(StringReader reader) throws CommandSyntaxException {
        var type = getNativeType().parse(reader);
        try {
            return codec.fromString(enumClass, type);
        } catch (IllegalArgumentException ignore) {
            throw new ComponentCommandExceptionType(
                    Component.text("No such enum constant: " + type)
            ).createWithContext(reader);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Arrays.stream(enumClass.getEnumConstants())
                .map(codec::toString)
                .map(StringArgumentType::escapeIfRequired)
                .filter(s -> s.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }
}

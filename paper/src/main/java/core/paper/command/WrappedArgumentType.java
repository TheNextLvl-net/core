package core.paper.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a wrapped implementation of CustomArgumentType.
 * This class wraps an existing ArgumentType and provides additional functionality.
 *
 * @param <T> the type of the native ArgumentType
 * @param <V> the type of the parsed value after conversion
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class WrappedArgumentType<T, V> implements CustomArgumentType<V, T> {
    private final ArgumentType<T> nativeType;
    private final ResultConverter<T, V> converter;
    private final SuggestionProvider suggestionProvider;

    /**
     * Constructs a WrappedArgumentType object.
     * This class wraps an existing ArgumentType and provides additional functionality.
     *
     * @param nativeType the native ArgumentType to be wrapped
     * @param converter  the ResultConverter used for converting parsed values
     */
    public WrappedArgumentType(ArgumentType<T> nativeType, ResultConverter<T, V> converter) {
        this(nativeType, converter, nativeType::listSuggestions);
    }

    @Override
    public final V parse(StringReader reader) throws CommandSyntaxException {
        return converter.convert(reader, nativeType.parse(reader));
    }

    @Override
    public final <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return suggestionProvider.suggest(context, builder);
    }
}

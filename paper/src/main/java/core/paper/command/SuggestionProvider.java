package core.paper.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

/**
 * Functional interface for providing suggestions for command arguments.
 */
@NullMarked
@FunctionalInterface
public interface SuggestionProvider {
    /**
     * Provides suggestions for command arguments.
     *
     * @param context The command context.
     * @param builder The suggestions builder.
     * @return A CompletableFuture containing the suggestions.
     */
    CompletableFuture<Suggestions> suggest(CommandContext<?> context, SuggestionsBuilder builder);
}

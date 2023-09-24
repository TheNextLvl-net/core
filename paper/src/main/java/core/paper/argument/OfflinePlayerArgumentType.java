package core.paper.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static core.paper.argument.PlayerArgumentType.ERROR_INVALID_UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OfflinePlayerArgumentType implements ArgumentType<OfflinePlayer> {
    private final LookupType type;

    /**
     * @see LookupType#UUID
     */
    public static OfflinePlayerArgumentType uuid() {
        return new OfflinePlayerArgumentType(LookupType.UUID);
    }

    /**
     * @see LookupType#CACHE
     */
    public static OfflinePlayerArgumentType cache() {
        return new OfflinePlayerArgumentType(LookupType.CACHE);
    }

    /**
     * @see LookupType#LOOKUP
     */
    public static OfflinePlayerArgumentType lookup() {
        return new OfflinePlayerArgumentType(LookupType.LOOKUP);
    }

    @Override
    public OfflinePlayer parse(StringReader reader) throws CommandSyntaxException {
        try {
            var input = reader.readUnquotedString();
            return switch (type) {
                case UUID -> Bukkit.getOfflinePlayer(UUID.fromString(input));
                case CACHE -> Bukkit.getOfflinePlayerIfCached(input);
                case LOOKUP -> Bukkit.getOfflinePlayer(input);
            };
        } catch (IllegalArgumentException e) {
            throw ERROR_INVALID_UUID.createWithContext(reader);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CompletableFuture.completedFuture(Suggestions.create(context.getInput(),
                Arrays.stream(Bukkit.getOfflinePlayers())
                        .map(player -> getType().equals(LookupType.UUID)
                                ? player.getUniqueId().toString()
                                : player.getName())
                        .filter(Objects::nonNull)
                        .map(name -> new Suggestion(context.getRange(), name))
                        .toList()));
    }

    @Override
    public String toString() {
        return "offlinePlayer(" + getType() + ")";
    }

    @Override
    public Collection<String> getExamples() {
        return type.getExamples();
    }

    @Getter
    @RequiredArgsConstructor
    public enum LookupType {
        /**
         * Look for an offline player by uuid
         *
         * @see Bukkit#getOfflinePlayer(UUID)
         */
        UUID(List.of(
                "05011e85-501c-432f-b9a6-8683c5fbd479",
                "069a79f4-44e9-4726-a5be-fca90e38aaf5"
        )),
        /**
         * Look for an offline player by name
         *
         * @see Bukkit#getOfflinePlayer(String)
         */
        LOOKUP(List.of(
                "NonSwag",
                "Notch"
        )),
        /**
         * Look for an offline player by name in the cache
         *
         * @see Bukkit#getOfflinePlayerIfCached(String)
         */
        CACHE(LOOKUP.getExamples());

        private final Collection<String> examples;
    }
}

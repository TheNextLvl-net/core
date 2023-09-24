package core.paper.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import core.paper.exception.PaperCommandExceptionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerArgumentType implements ArgumentType<Player> {
    public static final PaperCommandExceptionType UUID_ERROR = new PaperCommandExceptionType(
            Component.translatable("argument.uuid.invalid"));
    public static final PaperCommandExceptionType PLAYER_ERROR = new PaperCommandExceptionType(
            Component.translatable("argument.uuid.invalid"));
    private final LookupType type;

    /**
     * @see LookupType#UUID
     */
    public static PlayerArgumentType uuid() {
        return new PlayerArgumentType(LookupType.UUID);
    }

    /**
     * @see LookupType#EXACT
     */
    public static PlayerArgumentType exact() {
        return new PlayerArgumentType(LookupType.EXACT);
    }

    /**
     * @see LookupType#CLOSEST
     */
    public static PlayerArgumentType closest() {
        return new PlayerArgumentType(LookupType.CLOSEST);
    }

    @Override
    public Player parse(StringReader reader) throws CommandSyntaxException {
        try {
            var input = reader.readUnquotedString();
            var result = switch (type) {
                case UUID -> Bukkit.getPlayer(UUID.fromString(input));
                case EXACT -> Bukkit.getPlayerExact(input);
                case CLOSEST -> Bukkit.getPlayer(input);
            };
            if (result == null) throw PLAYER_ERROR.createWithContext(reader);
            return result;
        } catch (IllegalArgumentException e) {
            throw UUID_ERROR.createWithContext(reader);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CompletableFuture.completedFuture(Suggestions.create(context.getInput(),
                Bukkit.getOnlinePlayers().stream()
                        .map(player -> getType().equals(LookupType.UUID)
                                ? player.getUniqueId().toString()
                                : player.getName())
                        .map(s -> new Suggestion(context.getRange(), s))
                        .toList()));
    }

    @Override
    public String toString() {
        return "player(" + getType() + ")";
    }

    @Override
    public Collection<String> getExamples() {
        return type.getExamples();
    }

    @Getter
    @RequiredArgsConstructor
    public enum LookupType {
        /**
         * Look for a player based on their uuid
         *
         * @see Bukkit#getPlayer(UUID)
         */
        UUID(List.of(
                "05011e85-501c-432f-b9a6-8683c5fbd479",
                "069a79f4-44e9-4726-a5be-fca90e38aaf5"
        )),
        /**
         * Look for a player based on their name
         *
         * @see Bukkit#getPlayerExact(String)
         */
        EXACT(List.of(
                "NonSwag",
                "Notch"
        )),
        /**
         * Look for a player which name is closest to the input
         *
         * @see Bukkit#getPlayer(String)
         */
        CLOSEST(EXACT.getExamples());

        private final Collection<String> examples;
    }
}

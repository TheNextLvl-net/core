package core.paper.exception;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import core.annotation.ParametersAreNotNullByDefault;
import core.paper.brigadier.ComponentMessage;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

@Getter
@ParametersAreNotNullByDefault
public class PaperCommandException extends CommandSyntaxException {
    private final Component component;

    public PaperCommandException(PaperCommandExceptionType type, Component component) {
        this(type, component, null, -1);
    }

    public PaperCommandException(PaperCommandExceptionType type, Component component, ImmutableStringReader reader) {
        this(type, component, reader.getString(), reader.getCursor());
    }

    public PaperCommandException(PaperCommandExceptionType type, Component component, @Nullable String input, int cursor) {
        super(type, ComponentMessage.of(component), input, cursor);
        this.component = component;
    }
}

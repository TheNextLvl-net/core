package core.paper.brigadier.exceptions;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;

/**
 * Represents a custom exception type for component-based command errors.
 */
public final class ComponentCommandExceptionType extends SimpleCommandExceptionType {
    /**
     * Represents a custom exception type for component-based command errors.
     *
     * @param component The component describing the exception.
     */
    public ComponentCommandExceptionType(Component component) {
        super(MessageComponentSerializer.message().serialize(component));
    }
}

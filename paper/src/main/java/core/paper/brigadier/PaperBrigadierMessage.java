package core.paper.brigadier;

import com.mojang.brigadier.Message;
import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

/**
 * Represents an implementation of brigadier's {@link com.mojang.brigadier.Message},
 * providing support for {@link net.kyori.adventure.text.Component} messages.<br/>
 * <i>This class is inspired by Velocity</i>
 */
@FieldsAreNotNullByDefault
@ParametersAreNotNullByDefault
@MethodsReturnNotNullByDefault
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaperBrigadierMessage implements Message, ComponentLike {

    /**
     * Create a new tooltip message
     *
     * @param message the message component
     * @return the brigadier message
     */
    public static PaperBrigadierMessage tooltip(Component message) {
        return new PaperBrigadierMessage(message);
    }

    private final Component message;

    /**
     * Returns the message as a plain text.
     *
     * @return message as plain text
     */
    @Override
    public String getString() {
        return PlainTextComponentSerializer.plainText().serialize(message);
    }

    @Override
    public Component asComponent() {
        return this.message;
    }
}

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

@FieldsAreNotNullByDefault
@ParametersAreNotNullByDefault
@MethodsReturnNotNullByDefault
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ComponentMessage implements Message, ComponentLike {
    private final Component message;

    /**
     * Create a new component message
     *
     * @param component the component
     * @return the brigadier message
     */
    public static ComponentMessage of(Component component) {
        return new ComponentMessage(component);
    }

    @Override
    public String getString() {
        return PlainTextComponentSerializer.plainText().serialize(message);
    }

    @Override
    public Component asComponent() {
        return this.message;
    }
}

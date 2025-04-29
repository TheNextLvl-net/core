package core.i18n.file;

import com.mojang.brigadier.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

/**
 * Represents a {@link Message} containing a {@link Component}.
 */
public record ComponentMessage(Component component) implements Message {
    @Override
    public String getString() {
        return PlainTextComponentSerializer.plainText().serialize(component());
    }
}

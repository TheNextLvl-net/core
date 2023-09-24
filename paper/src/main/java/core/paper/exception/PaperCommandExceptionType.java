package core.paper.exception;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

@RequiredArgsConstructor
public class PaperCommandExceptionType implements CommandExceptionType {
    private final Component message;

    public PaperCommandException create() {
        return new PaperCommandException(this, message);
    }

    public PaperCommandException createWithContext(ImmutableStringReader reader) {
        return new PaperCommandException(this, message, reader);
    }

    @Override
    public String toString() {
        return PlainTextComponentSerializer.plainText().serialize(message);
    }
}

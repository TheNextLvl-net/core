package core.nbt.tag;

import core.nbt.NBTOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public interface Tag {

    /**
     * @return the name of this tag
     */
    @Nullable String getName();

    /**
     * @return the type of this tag
     */
    @NotNull Type getType();

    /**
     * Write the content of this tag to the given output stream
     *
     * @param outputStream the output stream to write to
     * @throws IOException thrown if something goes wrong
     */
    void write(@NotNull NBTOutputStream outputStream) throws IOException;

    default StringBuilder description() {
        var builder = new StringBuilder();
        if (getName() != null && !getName().isBlank()) builder.append("\"")
                .append(getName())
                .append("\": ");
        return builder;
    }

    /**
     * The type of tag
     *
     * @param name the name of the tag type
     * @param id   the id of the tag type
     */
    record Type(@NotNull String name, int id) {
    }
}

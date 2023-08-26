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
     * @return the type id of this tag
     */
    int getTypeId();

    /**
     * Write the content of this tag to the given output stream
     *
     * @param outputStream the output stream to write to
     * @throws IOException thrown if something goes wrong
     */
    void write(@NotNull NBTOutputStream outputStream) throws IOException;
}

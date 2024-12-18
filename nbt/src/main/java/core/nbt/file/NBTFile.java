package core.nbt.file;

import core.file.FileIO;
import core.io.IO;
import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import core.nbt.tag.CompoundTag;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.attribute.FileAttribute;

import static java.nio.file.StandardOpenOption.*;

/**
 * Represents an NBT (Named Binary Tag) file that can be read from and written to.
 * This class extends the {@link FileIO} class, allowing for operations specific to NBT files.
 *
 * @param <R> the type of the root object, which extends {@link CompoundTag}
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NBTFile<R extends CompoundTag> extends FileIO<R> {
    /**
     * The name of the root tag in an NBT (Named Binary Tag) file.
     * This variable can be null if no root name is specified.
     */
    private @Nullable String rootName;

    /**
     * Construct a new NBTFile providing a file, charset and default root object
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public NBTFile(@NonNull IO io, @NonNull Charset charset, R root) {
        super(io, charset, root);
    }

    /**
     * Construct a new NBTFile providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    public NBTFile(@NonNull IO io, R root) {
        super(io, root);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected R load() {
        if (!getIO().exists()) return getRoot();
        try (var inputStream = new NBTInputStream(
                getIO().inputStream(READ),
                getCharset()
        )) {
            var entry = inputStream.readNamedTag();
            entry.getValue().ifPresent(this::setRootName);
            return (R) entry.getKey();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NonNull FileIO<R> save(@NonNull FileAttribute<?>... attributes) {
        try {
            getIO().createParents(attributes);
            var root = getRoot();
            try (var outputStream = new NBTOutputStream(
                    getIO().outputStream(WRITE, CREATE, TRUNCATE_EXISTING),
                    getCharset()
            )) {
                outputStream.writeTag(getRootName(), root);
                return this;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

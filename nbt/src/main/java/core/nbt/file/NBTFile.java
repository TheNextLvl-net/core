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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.attribute.FileAttribute;

import static java.nio.file.StandardOpenOption.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NBTFile<R extends CompoundTag> extends FileIO<R> {
    private String rootName;

    /**
     * Construct a new NBTFile providing a file, charset and default root object
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public NBTFile(IO io, Charset charset, R root) {
        super(io, charset, root);
    }

    /**
     * Construct a new NBTFile providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    public NBTFile(IO io, R root) {
        super(io, root);
    }

    @Override
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
    public FileIO<R> save(FileAttribute<?>... attributes) {
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

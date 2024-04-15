package core.nbt.file;

import com.google.gson.reflect.TypeToken;
import core.file.FileIO;
import core.io.IO;
import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import core.nbt.snbt.SNBT;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.attribute.FileAttribute;

import static java.nio.file.StandardOpenOption.*;

@Getter
@Setter
@ToString(callSuper = true)
@Deprecated(forRemoval = true)
@EqualsAndHashCode(callSuper = true)
public class DataFile<R> extends FileIO<R> {
    private @Nullable String rootName;
    private final Type type;
    private final SNBT snbt;

    /**
     * Construct a new DataFile providing a file, default root object, type and snbt instance
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param type the root type
     * @param snbt the snbt instance
     */
    public DataFile(IO io, @Nullable R root, Type type, SNBT snbt) {
        super(io, root);
        this.type = type;
        this.snbt = snbt;
    }

    /**
     * Construct a new DataFile providing a file, type and snbt instance
     *
     * @param io   the file to read from and write to
     * @param type the root type
     * @param snbt the snbt instance
     */
    public DataFile(IO io, Type type, SNBT snbt) {
        this(io, null, type, snbt);
    }

    /**
     * Construct a new DataFile providing a file, default root object, type-token and snbt instance
     *
     * @param io    the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     * @param snbt  the snbt instance
     */
    public DataFile(IO io, @Nullable R root, TypeToken<R> token, SNBT snbt) {
        this(io, root, token.getType(), snbt);
    }

    /**
     * Construct a new DataFile providing a file, type-token and snbt instance
     *
     * @param io    the file to read from and write to
     * @param token the type-token
     * @param snbt  the snbt instance
     */
    public DataFile(IO io, TypeToken<R> token, SNBT snbt) {
        this(io, null, token, snbt);
    }

    /**
     * Construct a new DataFile providing a file, default root object and snbt instance
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param snbt the snbt instance
     */
    public DataFile(IO io, R root, SNBT snbt) {
        this(io, root, root.getClass(), snbt);
    }

    /**
     * Construct a new DataFile providing a file, default root object and type
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param type the root type
     */
    public DataFile(IO io, @Nullable R root, Type type) {
        this(io, root, type, new SNBT());
    }

    /**
     * Construct a new DataFile providing a file and type
     *
     * @param io   the file to read from and write to
     * @param type the root type
     */
    public DataFile(IO io, Type type) {
        this(io, null, type);
    }

    /**
     * Construct a new DataFile providing a file, default root object and type-token
     *
     * @param io    the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     */
    public DataFile(IO io, @Nullable R root, TypeToken<R> token) {
        this(io, root, token.getType());
    }

    /**
     * Construct a new DataFile providing a file and type-token
     *
     * @param io    the file to read from and write to
     * @param token the type-token
     */
    public DataFile(IO io, TypeToken<R> token) {
        this(io, null, token);
    }

    /**
     * Construct a new DataFile providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    public DataFile(IO io, R root) {
        this(io, root, root.getClass());
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
            return getSnbt().fromTag(entry.getKey(), getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileIO<R> save(FileAttribute<?>... attributes) {
        try {
            getIO().createParents(attributes);
            try (var outputStream = new NBTOutputStream(
                    getIO().outputStream(WRITE, CREATE, TRUNCATE_EXISTING),
                    getCharset()
            )) {
                outputStream.writeTag(getRootName(), getSnbt().toTag(getRoot(), getType()));
                return this;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

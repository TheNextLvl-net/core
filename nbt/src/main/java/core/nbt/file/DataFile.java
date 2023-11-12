package core.nbt.file;

import com.google.gson.reflect.TypeToken;
import core.api.file.FileIO;
import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import core.nbt.snbt.SNBT;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DataFile<R, T extends DataFile<R, T>> extends FileIO<R, T> {
    private @Nullable String rootName;
    private final Type type;
    private final SNBT snbt;

    /**
     * Construct a new DataFile providing a file, default root object, type and snbt instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     * @param snbt the snbt instance
     */
    public DataFile(File file, @Nullable R root, Type type, SNBT snbt) {
        super(file, root);
        this.type = type;
        this.snbt = snbt;
        setRoot(load());
    }

    /**
     * Construct a new DataFile providing a file, type and snbt instance
     *
     * @param file the file to read from and write to
     * @param type the root type
     * @param snbt the snbt instance
     */
    public DataFile(File file, Type type, SNBT snbt) {
        this(file, null, type, snbt);
    }

    /**
     * Construct a new DataFile providing a file, default root object, type-token and snbt instance
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     * @param snbt  the snbt instance
     */
    public DataFile(File file, @Nullable R root, TypeToken<R> token, SNBT snbt) {
        this(file, root, token.getType(), snbt);
    }

    /**
     * Construct a new DataFile providing a file, type-token and snbt instance
     *
     * @param file  the file to read from and write to
     * @param token the type-token
     * @param snbt  the snbt instance
     */
    public DataFile(File file, TypeToken<R> token, SNBT snbt) {
        this(file, null, token, snbt);
    }

    /**
     * Construct a new DataFile providing a file, default root object and snbt instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param snbt the snbt instance
     */
    public DataFile(File file, R root, SNBT snbt) {
        this(file, root, root.getClass(), snbt);
    }

    /**
     * Construct a new DataFile providing a file, default root object and type
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     */
    public DataFile(File file, @Nullable R root, Type type) {
        this(file, root, type, new SNBT());
    }

    /**
     * Construct a new DataFile providing a file and type
     *
     * @param file the file to read from and write to
     * @param type the root type
     */
    public DataFile(File file, Type type) {
        this(file, null, type);
    }

    /**
     * Construct a new DataFile providing a file, default root object and type-token
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     */
    public DataFile(File file, @Nullable R root, TypeToken<R> token) {
        this(file, root, token.getType());
    }

    /**
     * Construct a new DataFile providing a file and type-token
     *
     * @param file  the file to read from and write to
     * @param token the type-token
     */
    public DataFile(File file, TypeToken<R> token) {
        this(file, null, token);
    }

    /**
     * Construct a new DataFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public DataFile(File file, R root) {
        this(file, root, root.getClass());
    }

    @Override
    protected R load(File file) {
        if (!exists(file)) return getRoot();
        try (var inputStream = new NBTInputStream(new FileInputStream(file), getCharset())) {
            var entry = inputStream.readNamedTag();
            entry.getValue().ifPresent(this::setRootName);
            return getSnbt().fromTag(entry.getKey(), getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T save(File file) {
        try {
            createFile(file);
            try (var outputStream = new NBTOutputStream(new FileOutputStream(file), getCharset())) {
                outputStream.writeTag(getRootName(), getSnbt().toTag(getRoot(), getType()));
            }
            return (T) this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

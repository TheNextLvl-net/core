package core.nbt.file;

import com.google.gson.reflect.TypeToken;
import core.api.file.FileIO;
import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import core.nbt.snbt.SNBT;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

@Getter
public class DataFile<R> extends FileIO<R> {
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
    public R load() {
        if (!getFile().exists()) return getRoot();
        try (var inputStream = new NBTInputStream(new FileInputStream(getFile()), getCharset())) {
            return getSnbt().fromTag(inputStream.readTag(), getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataFile<R> save() {
        try {
            createFile();
            try (var outputStream = new NBTOutputStream(new FileOutputStream(getFile()), getCharset())) {
                outputStream.writeTag(getSnbt().toTag(getRoot(), getType()));
            }
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataFile<R> saveIfAbsent() {
        return (DataFile<R>) super.saveIfAbsent();
    }
}

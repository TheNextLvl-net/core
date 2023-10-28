package core.api.file.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import core.api.file.FileIO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GsonFile<R> extends FileIO<R> {
    private final Type type;
    private final Gson gson;

    /**
     * Construct a new GsonFile providing a file, default root object, type and gson instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     * @param gson the gson instance
     */
    public GsonFile(File file, @Nullable R root, Type type, Gson gson) {
        super(file, root);
        this.type = type;
        this.gson = gson;
        setRoot(load());
    }

    /**
     * Construct a new GsonFile providing a file, type and gson instance
     *
     * @param file the file to read from and write to
     * @param type the root type
     * @param gson the gson instance
     */
    public GsonFile(File file, Type type, Gson gson) {
        this(file, null, type, gson);
    }

    /**
     * Construct a new GsonFile providing a file, default root object, type-token and gson instance
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     * @param gson  the gson instance
     */
    public GsonFile(File file, @Nullable R root, TypeToken<R> token, Gson gson) {
        this(file, root, token.getType(), gson);
    }

    /**
     * Construct a new GsonFile providing a file, type-token and gson instance
     *
     * @param file  the file to read from and write to
     * @param token the type-token
     * @param gson  the gson instance
     */
    public GsonFile(File file, TypeToken<R> token, Gson gson) {
        this(file, null, token, gson);
    }

    /**
     * Construct a new GsonFile providing a file, default root object and gson instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param gson the gson instance
     */
    public GsonFile(File file, R root, Gson gson) {
        this(file, root, root.getClass(), gson);
    }

    /**
     * Construct a new GsonFile providing a file, default root object and type
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     */
    public GsonFile(File file, @Nullable R root, Type type) {
        this(file, root, type, new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create());
    }

    /**
     * Construct a new GsonFile providing a file and type
     *
     * @param file the file to read from and write to
     * @param type the root type
     */
    public GsonFile(File file, Type type) {
        this(file, null, type);
    }

    /**
     * Construct a new GsonFile providing a file, default root object and type-token
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     */
    public GsonFile(File file, @Nullable R root, TypeToken<R> token) {
        this(file, root, token.getType());
    }

    /**
     * Construct a new GsonFile providing a file and type-token
     *
     * @param file  the file to read from and write to
     * @param token the type-token
     */
    public GsonFile(File file, TypeToken<R> token) {
        this(file, null, token);
    }

    /**
     * Construct a new GsonFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public GsonFile(File file, R root) {
        this(file, root, root.getClass());
    }

    @Override
    public R load() {
        if (!getFile().exists()) return getRoot();
        try (FileReader reader = new FileReader(getFile(), getCharset())) {
            return getGson().fromJson(reader, getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GsonFile<R> save() {
        try {
            createFile();
            try (FileWriter writer = new FileWriter(getFile(), getCharset())) {
                getGson().toJson(getRoot(), getType(), writer);
            }
            return this;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public GsonFile<R> saveIfAbsent() {
        return (GsonFile<R>) super.saveIfAbsent();
    }
}

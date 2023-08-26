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
@ToString
@EqualsAndHashCode(callSuper = false)
public class GsonFile<R> extends FileIO<R> {
    private final Type type;
    private final Gson gson;

    public GsonFile(File file, @Nullable R root, Type type, Gson gson) {
        super(file, root);
        this.type = type;
        this.gson = gson;
        setRoot(load());
    }

    public GsonFile(File file, Type type, Gson gson) {
        this(file, null, type, gson);
    }

    public GsonFile(File file, @Nullable R root, TypeToken<R> token, Gson gson) {
        this(file, root, token.getType(), gson);
    }

    public GsonFile(File file, TypeToken<R> token, Gson gson) {
        this(file, null, token, gson);
    }

    public GsonFile(File file, R root, Gson gson) {
        this(file, root, root.getClass(), gson);
    }

    public GsonFile(File file, @Nullable R root, Type type) {
        this(file, root, type, new GsonBuilder().setPrettyPrinting().serializeNulls().create());
    }

    public GsonFile(File file, Type type) {
        this(file, null, type);
    }

    public GsonFile(File file, @Nullable R root, TypeToken<R> token) {
        this(file, root, token.getType());
    }

    public GsonFile(File file, TypeToken<R> token) {
        this(file, null, token);
    }

    public GsonFile(File file, R root) {
        this(file, root, root.getClass());
    }

    public GsonBuilder load(GsonBuilder builder) {
        return builder;
    }

    @Override
    public R load() {
        if (!getFile().exists()) return getRoot();
        try (FileReader reader = new FileReader(getFile(), getCharset())) {
            return gson.fromJson(reader, getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            createFile();
            try (FileWriter writer = new FileWriter(getFile(), getCharset())) {
                gson.toJson(getRoot(), writer);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}

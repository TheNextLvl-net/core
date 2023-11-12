package core.api.file.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import core.api.file.FileIO;
import core.api.file.Validatable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GsonFile<R, T extends GsonFile<R, T>> extends FileIO<R, T> implements Validatable<T> {
    protected final @Nullable R defaultRoot;
    private final @Getter Type type;
    private final @Getter Gson gson;

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
        this.defaultRoot = root;
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
    protected R load(File file) {
        if (!file.exists()) return getRoot();
        try (FileReader reader = new FileReader(file, getCharset())) {
            return getGson().fromJson(reader, getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T save(File file) {
        try {
            createFile(file);
            try (FileWriter writer = new FileWriter(file, getCharset())) {
                getGson().toJson(getRoot(), getType(), writer);
            }
            return (T) this;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public T validate(Scope scope) {
        if (!exists()) return (T) this;
        var defaultTree = getGson().toJsonTree(defaultRoot, getType());
        var currentTree = getGson().toJsonTree(getRoot(), getType());
        var validatedTree = validate(scope, defaultTree, currentTree);
        if (currentTree.equals(validatedTree)) return (T) this;
        return setRoot(getGson().fromJson(validatedTree, getType()));
    }

    private static JsonElement validate(Scope scope, JsonElement defaultTree, JsonElement currentTree) {
        if (!defaultTree.isJsonObject() || !currentTree.isJsonObject()) return currentTree;
        return validate(scope, defaultTree.getAsJsonObject(), currentTree.getAsJsonObject());
    }

    private static JsonObject validate(Scope scope, JsonObject defaultTree, JsonObject currentTree) {
        var currentCopy = currentTree.deepCopy();
        if (scope.isFiltering()) filterUnused(defaultTree, currentCopy);
        if (scope.isFilling()) fillMissing(defaultTree, currentCopy);
        return currentCopy;
    }

    private static void fillMissing(JsonObject defaultTree, JsonObject currentCopy) {
        defaultTree.entrySet().stream()
                .filter(entry -> !currentCopy.has(entry.getKey()))
                .forEach(entry -> currentCopy.add(entry.getKey(), entry.getValue()));
    }

    private static void filterUnused(JsonObject defaultTree, JsonObject currentCopy) {
        currentCopy.entrySet().removeIf(entry -> !defaultTree.has(entry.getKey()));
    }
}

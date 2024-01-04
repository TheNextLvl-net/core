package core.file.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import core.file.FileIO;
import core.file.Validatable;
import core.io.IO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.file.attribute.FileAttribute;

import static java.nio.file.StandardOpenOption.*;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GsonFile<R> extends FileIO<R> implements Validatable<R> {
    protected final @Nullable R defaultRoot;
    private final @Getter Type type;
    private final @Getter Gson gson;

    /**
     * Construct a new GsonFile providing a file, default root object, type and gson instance
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param type the root type
     * @param gson the gson instance
     */
    public GsonFile(IO io, @Nullable R root, Type type, Gson gson) {
        super(io, root);
        this.defaultRoot = root;
        this.type = type;
        this.gson = gson;
    }

    /**
     * Construct a new GsonFile providing a file, type and gson instance
     *
     * @param io   the file to read from and write to
     * @param type the root type
     * @param gson the gson instance
     */
    public GsonFile(IO io, Type type, Gson gson) {
        this(io, null, type, gson);
    }

    /**
     * Construct a new GsonFile providing a file, default root object, type-token and gson instance
     *
     * @param io    the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     * @param gson  the gson instance
     */
    public GsonFile(IO io, @Nullable R root, TypeToken<R> token, Gson gson) {
        this(io, root, token.getType(), gson);
    }

    /**
     * Construct a new GsonFile providing a file, type-token and gson instance
     *
     * @param io    the file to read from and write to
     * @param token the type-token
     * @param gson  the gson instance
     */
    public GsonFile(IO io, TypeToken<R> token, Gson gson) {
        this(io, null, token, gson);
    }

    /**
     * Construct a new GsonFile providing a file, default root object and gson instance
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param gson the gson instance
     */
    public GsonFile(IO io, R root, Gson gson) {
        this(io, root, root.getClass(), gson);
    }

    /**
     * Construct a new GsonFile providing a file, default root object and type
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param type the root type
     */
    public GsonFile(IO io, @Nullable R root, Type type) {
        this(io, root, type, new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create());
    }

    /**
     * Construct a new GsonFile providing a file and type
     *
     * @param io   the file to read from and write to
     * @param type the root type
     */
    public GsonFile(IO io, Type type) {
        this(io, null, type);
    }

    /**
     * Construct a new GsonFile providing a file, default root object and type-token
     *
     * @param io    the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     */
    public GsonFile(IO io, @Nullable R root, TypeToken<R> token) {
        this(io, root, token.getType());
    }

    /**
     * Construct a new GsonFile providing a file and type-token
     *
     * @param io    the file to read from and write to
     * @param token the type-token
     */
    public GsonFile(IO io, TypeToken<R> token) {
        this(io, null, token);
    }

    /**
     * Construct a new GsonFile providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    public GsonFile(IO io, R root) {
        this(io, root, root.getClass());
    }

    @Override
    protected R load() {
        if (!getIO().exists()) return defaultRoot;
        try (var reader = new JsonReader(new InputStreamReader(
                getIO().inputStream(READ),
                getCharset()
        ))) {
            return getGson().fromJson(reader, getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileIO<R> save(FileAttribute<?>... attributes) {
        try {
            getIO().createParents(attributes);
            try (var writer = new BufferedWriter(new OutputStreamWriter(
                    getIO().outputStream(WRITE, CREATE, TRUNCATE_EXISTING),
                    getCharset()
            ))) {
                getGson().toJson(getRoot(), getType(), writer);
                return this;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileIO<R> validate(Scope scope) {
        if (!getIO().exists()) return this;
        var defaultTree = getGson().toJsonTree(defaultRoot, getType());
        var currentTree = getGson().toJsonTree(getRoot(), getType());
        var validatedTree = validate(scope, defaultTree, currentTree);
        if (currentTree.equals(validatedTree)) return this;
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

package core.file.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import core.file.FileIO;
import core.file.Validatable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * The {@code GsonFile} class extends {@code FileIO} to provide methods for reading
 * and writing JSON data using the Gson library.
 * This class supports validation of JSON structures and handling default values for root elements.
 *
 * @param <R> the type of the root object in the JSON structure
 */
@NullMarked
public class GsonFile<R> extends FileIO<R> implements Validatable<R> {
    /**
     * The default root object associated with this GsonFile.
     * It may be null if no default root object is provided.
     */
    protected final @Nullable R defaultRoot;
    private final Type type;
    private final Gson gson;

    /**
     * Construct a new GsonFile providing a file, default root object, type, and gson instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     * @param gson the gson instance
     */
    public GsonFile(Path file, @Nullable R root, Type type, Gson gson) {
        super(file, root);
        this.defaultRoot = root;
        this.type = type;
        this.gson = gson;
    }

    /**
     * Construct a new GsonFile providing a file, type and gson instance
     *
     * @param file the file to read from and write to
     * @param type the root type
     * @param gson the gson instance
     */
    public GsonFile(Path file, Type type, Gson gson) {
        this(file, null, type, gson);
    }

    /**
     * Construct a new GsonFile providing a file, default root object, type-token, and gson instance
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     * @param gson  the gson instance
     */
    public GsonFile(Path file, @Nullable R root, TypeToken<R> token, Gson gson) {
        this(file, root, token.getType(), gson);
    }

    /**
     * Construct a new GsonFile providing a file, type-token, and gson instance
     *
     * @param file  the file to read from and write to
     * @param token the type-token
     * @param gson  the gson instance
     */
    public GsonFile(Path file, TypeToken<R> token, Gson gson) {
        this(file, null, token, gson);
    }

    /**
     * Construct a new GsonFile providing a file, default root object and gson instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param gson the gson instance
     */
    public GsonFile(Path file, R root, Gson gson) {
        this(file, root, root.getClass(), gson);
    }

    /**
     * Construct a new GsonFile providing a file, default root object and type
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     */
    public GsonFile(Path file, @Nullable R root, Type type) {
        this(file, root, type, new GsonBuilder()
                .disableHtmlEscaping()
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
    public GsonFile(Path file, Type type) {
        this(file, null, type);
    }

    /**
     * Construct a new GsonFile providing a file, default root object, and type-token
     *
     * @param file    the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     */
    public GsonFile(Path file, @Nullable R root, TypeToken<R> token) {
        this(file, root, token.getType());
    }

    /**
     * Construct a new GsonFile providing a file and type-token
     *
     * @param file  the file to read from and write to
     * @param token the type-token
     */
    public GsonFile(Path file, TypeToken<R> token) {
        this(file, null, token);
    }

    /**
     * Construct a new GsonFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public GsonFile(Path file, R root) {
        this(file, root, root.getClass());
    }

    @Override
    protected @Nullable R load() {
        if (!Files.isRegularFile(getFile())) return getRoot();
        try (var reader = new JsonReader(new InputStreamReader(
                Files.newInputStream(getFile(), READ),
                getCharset()
        ))) {
            R root = getGson().fromJson(reader, getType());
            return root != null ? root : defaultRoot;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @NullMarked
    public FileIO<R> save(FileAttribute<?>... attributes) {
        try {
            var root = getRoot();
            Files.createDirectories(getFile().getParent());
            try (var writer = new BufferedWriter(new OutputStreamWriter(
                    Files.newOutputStream(getFile(), WRITE, CREATE, TRUNCATE_EXISTING),
                    getCharset()
            ))) {
                getGson().toJson(root, getType(), writer);
                return this;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileIO<R> validate(Scope scope) {
        if (!Files.isRegularFile(getFile())) return this;
        var defaultTree = getGson().toJsonTree(defaultRoot, getType());
        var currentTree = getGson().toJsonTree(getRoot(), getType());
        var validatedTree = validate(scope, defaultTree, currentTree);
        if (currentTree.equals(validatedTree)) return this;
        return setRoot(getGson().fromJson(validatedTree, getType()));
    }

    /**
     * Retrieves the Gson instance associated with this class.
     *
     * @return the Gson instance used for JSON serialization and deserialization
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * Retrieves the type of object stored by this instance.
     *
     * @return the type of object stored by this instance
     */
    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GsonFile<?> gsonFile = (GsonFile<?>) o;
        return Objects.equals(defaultRoot, gsonFile.defaultRoot) && Objects.equals(type, gsonFile.type) && Objects.equals(gson, gsonFile.gson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), defaultRoot, type, gson);
    }

    @Override
    public String toString() {
        return "GsonFile{" +
                "defaultRoot=" + defaultRoot +
                ", type=" + type +
                ", gson=" + gson +
                "} " + super.toString();
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

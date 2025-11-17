package core.file.format;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;
import java.nio.file.Path;

/**
 * A class for handling JSON files using Gson.
 * This class extends from GsonFile and provides various constructors
 * to initialize a JsonFile object with different parameters,
 * including a file (IO object), root object, type, type-token, and Gson instance.
 *
 * @param <R> the type of the root object extending from JsonElement
 */
@NullMarked
public class JsonFile<R extends JsonElement> extends GsonFile<R> {
    /**
     * Construct a new JsonFile providing a file, default root object, type, and gson instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     * @param gson the gson instance
     */
    public JsonFile(Path file, R root, Type type, Gson gson) {
        super(file, root, type, gson);
    }

    /**
     * Construct a new JsonFile providing a file, type and gson instance
     *
     * @param file the file to read from and write to
     * @param type the root type
     * @param gson the gson instance
     */
    public JsonFile(Path file, Type type, Gson gson) {
        super(file, type, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object, type-token, and gson instance
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     * @param gson  the gson instance
     */
    public JsonFile(Path file, R root, TypeToken<R> token, Gson gson) {
        super(file, root, token, gson);
    }

    /**
     * Construct a new JsonFile providing a file, type-token, and gson instance
     *
     * @param file  the file to read from and write to
     * @param token the type-token
     * @param gson  the gson instance
     */
    public JsonFile(Path file, TypeToken<R> token, Gson gson) {
        super(file, token, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object and gson instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param gson the gson instance
     */
    public JsonFile(Path file, R root, Gson gson) {
        super(file, root, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object and type
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     */
    public JsonFile(Path file, R root, Type type) {
        super(file, root, type);
    }

    /**
     * Construct a new JsonFile providing a file, default root object, and type-token
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     */
    public JsonFile(Path file, R root, TypeToken<R> token) {
        super(file, root, token.getType());
    }

    /**
     * Construct a new JsonFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public JsonFile(Path file, R root) {
        super(file, root, root.getClass());
    }
}


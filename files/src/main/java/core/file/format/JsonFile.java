package core.file.format;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import core.io.IO;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Type;

/**
 * A class for handling JSON files using Gson.
 * This class extends from GsonFile and provides various constructors
 * to initialize a JsonFile object with different parameters,
 * including a file (IO object), root object, type, type-token, and Gson instance.
 *
 * @param <R> the type of the root object extending from JsonElement
 */
@NullMarked
public class JsonFile<R extends JsonElement> extends GsonFile<@NonNull R> {
    /**
     * Construct a new JsonFile providing a file, default root object, type, and gson instance
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param type the root type
     * @param gson the gson instance
     */
    public JsonFile(@NonNull IO io, @NonNull R root, @NonNull Type type, @NonNull Gson gson) {
        super(io, root, type, gson);
    }

    /**
     * Construct a new JsonFile providing a file, type and gson instance
     *
     * @param io   the file to read from and write to
     * @param type the root type
     * @param gson the gson instance
     */
    public JsonFile(@NonNull IO io, @NonNull Type type, @NonNull Gson gson) {
        super(io, type, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object, type-token, and gson instance
     *
     * @param io    the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     * @param gson  the gson instance
     */
    public JsonFile(@NonNull IO io, @NonNull R root, @NonNull TypeToken<R> token, @NonNull Gson gson) {
        super(io, root, token, gson);
    }

    /**
     * Construct a new JsonFile providing a file, type-token, and gson instance
     *
     * @param io    the file to read from and write to
     * @param token the type-token
     * @param gson  the gson instance
     */
    public JsonFile(@NonNull IO io, @NonNull TypeToken<R> token, @NonNull Gson gson) {
        super(io, token, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object and gson instance
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param gson the gson instance
     */
    public JsonFile(@NonNull IO io, @NonNull R root, @NonNull Gson gson) {
        super(io, root, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object and type
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param type the root type
     */
    public JsonFile(@NonNull IO io, @NonNull R root, @NonNull Type type) {
        super(io, root, type);
    }

    /**
     * Construct a new JsonFile providing a file, default root object, and type-token
     *
     * @param io    the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     */
    public JsonFile(@NonNull IO io, @NonNull R root, @NonNull TypeToken<R> token) {
        this(io, root, token.getType());
    }

    /**
     * Construct a new JsonFile providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    public JsonFile(@NonNull IO io, @NonNull R root) {
        this(io, root, root.getClass());
    }
}


package core.file.format;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import core.io.IO;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class JsonFile<R extends JsonElement> extends GsonFile<@NotNull R> {
    /**
     * Construct a new JsonFile providing a file, default root object, type and gson instance
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param type the root type
     * @param gson the gson instance
     */
    public JsonFile(IO io, R root, Type type, Gson gson) {
        super(io, root, type, gson);
    }

    /**
     * Construct a new JsonFile providing a file, type and gson instance
     *
     * @param io   the file to read from and write to
     * @param type the root type
     * @param gson the gson instance
     */
    public JsonFile(IO io, Type type, Gson gson) {
        super(io, type, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object, type-token and gson instance
     *
     * @param io    the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     * @param gson  the gson instance
     */
    public JsonFile(IO io, R root, TypeToken<R> token, Gson gson) {
        super(io, root, token, gson);
    }

    /**
     * Construct a new JsonFile providing a file, type-token and gson instance
     *
     * @param io    the file to read from and write to
     * @param token the type-token
     * @param gson  the gson instance
     */
    public JsonFile(IO io, TypeToken<R> token, Gson gson) {
        super(io, token, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object and gson instance
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param gson the gson instance
     */
    public JsonFile(IO io, R root, Gson gson) {
        super(io, root, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object and type
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     * @param type the root type
     */
    public JsonFile(IO io, R root, Type type) {
        super(io, root, type);
    }

    /**
     * Construct a new JsonFile providing a file, default root object and type-token
     *
     * @param io    the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     */
    public JsonFile(IO io, R root, TypeToken<R> token) {
        this(io, root, token.getType());
    }

    /**
     * Construct a new JsonFile providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    public JsonFile(IO io, R root) {
        this(io, root, root.getClass());
    }
}


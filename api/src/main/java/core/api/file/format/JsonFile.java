package core.api.file.format;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Type;

public class JsonFile<R extends JsonElement> extends GsonFile<@NotNull R> {
    /**
     * Construct a new JsonFile providing a file, default root object, type and gson instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     * @param gson the gson instance
     */
    public JsonFile(File file, R root, Type type, Gson gson) {
        super(file, root, type, gson);
    }

    /**
     * Construct a new JsonFile providing a file, type and gson instance
     *
     * @param file the file to read from and write to
     * @param type the root type
     * @param gson the gson instance
     */
    public JsonFile(File file, Type type, Gson gson) {
        super(file, type, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object, type-token and gson instance
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     * @param gson  the gson instance
     */
    public JsonFile(File file, R root, TypeToken<R> token, Gson gson) {
        super(file, root, token, gson);
    }

    /**
     * Construct a new JsonFile providing a file, type-token and gson instance
     *
     * @param file  the file to read from and write to
     * @param token the type-token
     * @param gson  the gson instance
     */
    public JsonFile(File file, TypeToken<R> token, Gson gson) {
        super(file, token, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object and gson instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param gson the gson instance
     */
    public JsonFile(File file, R root, Gson gson) {
        super(file, root, gson);
    }

    /**
     * Construct a new JsonFile providing a file, default root object and type
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     */
    public JsonFile(File file, R root, Type type) {
        super(file, root, type);
    }

    /**
     * Construct a new JsonFile providing a file, default root object and type-token
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     */
    public JsonFile(File file, R root, TypeToken<R> token) {
        this(file, root, token.getType());
    }

    /**
     * Construct a new JsonFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public JsonFile(File file, R root) {
        this(file, root, root.getClass());
    }
}


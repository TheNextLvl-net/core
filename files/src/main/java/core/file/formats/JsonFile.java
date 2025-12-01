package core.file.formats;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.jspecify.annotations.NullMarked;

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
     * Construct a new JsonFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public JsonFile(Path file, R root) {
        super(file, root);
    }
}


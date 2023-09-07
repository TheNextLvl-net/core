package core.api.file.format;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Type;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class JsonFile<R extends JsonElement> extends GsonFile<R> {
    /**
     * Construct a new JsonFile providing a file, default root object, type and gson instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     * @param gson the gson instance
     */
    public JsonFile(File file, @Nullable R root, Type type, Gson gson) {
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
    public JsonFile(File file, @Nullable R root, TypeToken<R> token, Gson gson) {
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
    public JsonFile(File file, @Nullable R root, Type type) {
        super(file, root, type);
    }

    /**
     * Construct a new JsonFile providing a file and type
     *
     * @param file the file to read from and write to
     * @param type the root type
     */
    public JsonFile(File file, Type type) {
        super(file, type);
    }

    /**
     * Construct a new JsonFile providing a file, default root object and type-token
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     */
    public JsonFile(File file, @Nullable R root, TypeToken<R> token) {
        super(file, root, token);
    }

    /**
     * Construct a new JsonFile providing a file and type-token
     *
     * @param file  the file to read from and write to
     * @param token the type-token
     */
    public JsonFile(File file, TypeToken<R> token) {
        super(file, token);
    }

    /**
     * Construct a new JsonFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public JsonFile(File file, R root) {
        super(file, root);
    }

    /**
     * Construct a new JsonFile providing a file
     *
     * @param file the file to read from and write to
     */
    public JsonFile(File file) {
        super(file, JsonElement.class);
    }

    @Override
    public JsonFile<R> save() {
        return (JsonFile<R>) super.save();
    }

    @Override
    public JsonFile<R> saveIfAbsent() {
        return (JsonFile<R>) super.saveIfAbsent();
    }
}


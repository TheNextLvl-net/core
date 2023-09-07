package core.nbt.file;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Type;

@ApiStatus.Experimental
public class SNBTFile<R extends JsonElement> extends DataFile<R> {
    /**
     * Construct a new SNBTFile providing a file, default root object, type and gson instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     * @param gson the gson instance
     */
    SNBTFile(File file, @Nullable R root, Type type, Gson gson) { // TODO: 07.09.23 make this somehow public
        super(file, root, type, gson);
    }

    /**
     * Construct a new SNBTFile providing a file, type and gson instance
     *
     * @param file the file to read from and write to
     * @param type the root type
     * @param gson the gson instance
     */
    SNBTFile(File file, Type type, Gson gson) { // TODO: 07.09.23 make this somehow public
        super(file, type, gson);
    }

    /**
     * Construct a new SNBTFile providing a file, default root object, type-token and gson instance
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     * @param gson  the gson instance
     */
    SNBTFile(File file, @Nullable R root, TypeToken<R> token, Gson gson) { // TODO: 07.09.23 make this somehow public
        super(file, root, token, gson);
    }

    /**
     * Construct a new SNBTFile providing a file, type-token and gson instance
     *
     * @param file  the file to read from and write to
     * @param token the type-token
     * @param gson  the gson instance
     */
    SNBTFile(File file, TypeToken<R> token, Gson gson) { // TODO: 07.09.23 make this somehow public
        super(file, token, gson);
    }

    /**
     * Construct a new SNBTFile providing a file, default root object and gson instance
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param gson the gson instance
     */
    SNBTFile(File file, R root, Gson gson) { // TODO: 07.09.23 make this somehow public
        super(file, root, gson);
    }

    /**
     * Construct a new SNBTFile providing a file, default root object and type
     *
     * @param file the file to read from and write to
     * @param root the default root object
     * @param type the root type
     */
    public SNBTFile(File file, @Nullable R root, Type type) {
        super(file, root, type);
    }

    /**
     * Construct a new SNBTFile providing a file and type
     *
     * @param file the file to read from and write to
     * @param type the root type
     */
    public SNBTFile(File file, Type type) {
        super(file, type);
    }

    /**
     * Construct a new SNBTFile providing a file, default root object and type-token
     *
     * @param file  the file to read from and write to
     * @param root  the default root object
     * @param token the type-token
     */
    public SNBTFile(File file, @Nullable R root, TypeToken<R> token) {
        super(file, root, token);
    }

    /**
     * Construct a new SNBTFile providing a file and type-token
     *
     * @param file  the file to read from and write to
     * @param token the type-token
     */
    public SNBTFile(File file, TypeToken<R> token) {
        super(file, token);
    }

    /**
     * Construct a new SNBTFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public SNBTFile(File file, R root) {
        super(file, root);
    }

    /**
     * Construct a new SNBTFile providing a file
     *
     * @param file the file to read from and write to
     */
    public SNBTFile(File file) {
        super(file, JsonElement.class);
    }

    @Override
    public SNBTFile<R> save() {
        return (SNBTFile<R>) super.save();
    }

    @Override
    public SNBTFile<R> saveIfAbsent() {
        return (SNBTFile<R>) super.saveIfAbsent();
    }
}

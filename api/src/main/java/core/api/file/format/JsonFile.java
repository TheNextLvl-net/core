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
    public JsonFile(File file, @Nullable R root, Type type, Gson gson) {
        super(file, root, type, gson);
    }

    public JsonFile(File file, Type type, Gson gson) {
        super(file, type, gson);
    }

    public JsonFile(File file, @Nullable R root, TypeToken<R> token, Gson gson) {
        super(file, root, token, gson);
    }

    public JsonFile(File file, TypeToken<R> token, Gson gson) {
        super(file, token, gson);
    }

    public JsonFile(File file, R root, Gson gson) {
        super(file, root, gson);
    }

    public JsonFile(File file, @Nullable R root, Type type) {
        super(file, root, type);
    }

    public JsonFile(File file, Type type) {
        super(file, type);
    }

    public JsonFile(File file, @Nullable R root, TypeToken<R> token) {
        super(file, root, token);
    }

    public JsonFile(File file, TypeToken<R> token) {
        super(file, token);
    }

    public JsonFile(File file, R root) {
        super(file, root);
    }

    public JsonFile(File file) {
        super(file, JsonElement.class);
    }
}


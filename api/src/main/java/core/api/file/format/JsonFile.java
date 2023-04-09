package core.api.file.format;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import core.api.file.FileIO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class JsonFile extends FileIO<JsonElement> {
    private boolean lenient = false;

    public JsonFile(File file) {
        super(file);
    }

    public JsonFile(String file) {
        this(new File(file));
    }

    public JsonFile(File parent, String child) {
        this(new File(parent, child));
    }

    public JsonFile(String parent, String child) {
        this(new File(parent, child));
    }

    @Override
    public JsonElement load() {
        if (!getFile().exists()) return new JsonObject();
        try (var reader = Files.newBufferedReader(getFile().toPath(), getCharset())) {
            var jsonReader = new JsonReader(reader);
            jsonReader.setLenient(isLenient());
            return Streams.parse(jsonReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            createFile();
            Files.writeString(getFile().toPath(), new GsonBuilder().setPrettyPrinting().create().toJson(getRoot()), getCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


package core.nbt.file;

import com.google.gson.JsonElement;

import java.io.File;

public class SNBTFile extends DataFile<JsonElement> {

    public SNBTFile(File file, JsonElement root) {
        super(file, root);
    }

    public SNBTFile(File file) {
        super(file, JsonElement.class);
    }
}

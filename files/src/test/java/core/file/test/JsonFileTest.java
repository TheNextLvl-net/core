package core.file.test;

import com.google.gson.JsonObject;
import core.file.format.JsonFile;
import core.io.IO;

public class JsonFileTest {
    public static void main(String[] args) {
        var file = new JsonFile<>(IO.of("hey", "test.json"), new JsonObject());
        System.out.println(file.getRoot());
        file.getRoot().addProperty("test", 1);
        file.save();
    }
}

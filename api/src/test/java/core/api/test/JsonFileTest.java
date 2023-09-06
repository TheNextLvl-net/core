package core.api.test;

import com.google.gson.JsonArray;
import core.api.file.format.JsonFile;

import java.io.File;

public class JsonFileTest {
    public static void main(String[] args) {
        var file = new JsonFile<>(new File("test.json"), new JsonArray());
        file.getRoot().forEach(System.out::println);
        file.save();
    }
}

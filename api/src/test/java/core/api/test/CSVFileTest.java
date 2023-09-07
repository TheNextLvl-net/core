package core.api.test;

import core.api.file.format.separator.CSVFile;

import java.io.File;
import java.util.List;

public class CSVFileTest {
    public static void main(String[] args) {
        var file = new CSVFile(new File("test.csv"), List.of(
                List.of("test", "test2"),
                List.of("lol", "lol2"))
        ).saveIfAbsent();
        file.getRoot().forEach(System.out::println);
    }
}

package core.api.test;

import core.api.file.format.separator.TSVFile;

import java.io.File;
import java.util.List;

public class TSVFileTest {
    public static void main(String[] args) {
        var file = new TSVFile<>(new File("test.tsv"), List.of(
                List.of("test", "test2"),
                List.of("lol", "lol2"))
        ).saveIfAbsent();
        file.getRoot().forEach(System.out::println);
    }
}

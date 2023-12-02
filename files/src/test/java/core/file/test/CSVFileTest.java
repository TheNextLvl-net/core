package core.file.test;

import core.file.format.separator.CSVFile;
import core.io.IO;

import java.util.List;

public class CSVFileTest {
    public static void main(String[] args) {
        var file = new CSVFile(IO.of("lol", "hey", "test.csv"), List.of(
                List.of("test", "test2"),
                List.of("lol", "lol2"))
        ).saveIfAbsent();
        file.getRoot().forEach(System.out::println);
    }
}

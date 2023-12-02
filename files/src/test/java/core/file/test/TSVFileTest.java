package core.file.test;

import core.io.IO;
import core.file.format.separator.TSVFile;

import java.util.List;

public class TSVFileTest {
    public static void main(String[] args) {
        var file = new TSVFile(IO.of("test.tsv"), List.of(
                List.of("test", "test2"),
                List.of("lol", "lol2"))
        ).saveIfAbsent();
        file.getRoot().forEach(System.out::println);
    }
}

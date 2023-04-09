package core.api.test;

import core.api.file.format.ScriptFile;

import java.io.File;
import java.io.IOException;

public class ScriptFileTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        ScriptFile file = new ScriptFile(new File("tests", "script.sh")).deleteAfterRun(true);
        file.run(builder -> {
            builder.redirectOutput(new File("tests", "logs.txt"));
            builder.redirectError(new File("tests", "logs.txt"));
            return builder;
        });
    }
}

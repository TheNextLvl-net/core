package core.file.test;

import core.io.IO;
import core.file.format.ScriptFile;

import java.io.IOException;
import java.util.List;

public class ScriptFileTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        var file = new ScriptFile(IO.of("script.sh"), List.of("echo error", "exit 1"))
                .deletion(ScriptFile.Deletion.ALWAYS)
                .redirect(ProcessBuilder.Redirect.INHERIT);
        file.save();
        System.out.println(file.run().exitValue());
    }
}

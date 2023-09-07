package core.api.test;

import core.api.file.format.ScriptFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ScriptFileTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        var file = new ScriptFile(new File("script.sh"), List.of("echo error", "exit 1"))
                .deletion(ScriptFile.Deletion.ALWAYS)
                .redirect(ProcessBuilder.Redirect.INHERIT);
        System.out.println(file.save().run().exitValue());
    }
}

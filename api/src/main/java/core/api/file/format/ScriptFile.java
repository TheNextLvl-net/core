package core.api.file.format;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class ScriptFile extends TextFile {
    private boolean deleteAfterRun;

    public ScriptFile(File file) {
        super(file);
    }

    public ScriptFile(String file) {
        this(new File(file));
    }

    public ScriptFile(File parent, String child) {
        this(new File(parent, child));
    }

    public ScriptFile(String parent, String child) {
        this(new File(parent, child));
    }

    public int run(Function<ProcessBuilder, ProcessBuilder> consumer) throws IOException, InterruptedException {
        var builder = new ProcessBuilder("bash", getFile().getAbsolutePath())
                .directory(getFile().getAbsoluteFile().getParentFile());
        var process = consumer.apply(builder).start();
        process.waitFor();
        if (deleteAfterRun()) delete();
        return process.exitValue();
    }
}

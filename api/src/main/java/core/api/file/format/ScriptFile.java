package core.api.file.format;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true, fluent = true)
public class ScriptFile extends TextFile {
    /**
     * Whether to delete the file under certain circumstances
     */
    private Deletion deletion = Deletion.NEVER;
    /**
     * Where to redirect the output to
     */
    private ProcessBuilder.Redirect redirect = ProcessBuilder.Redirect.DISCARD;

    /**
     * Construct a new ScriptFile providing a file, charset and default root object
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public ScriptFile(File file, Charset charset, List<String> root) {
        super(file, charset, root);
    }

    /**
     * Construct a new ScriptFile providing a file and charset
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    public ScriptFile(File file, Charset charset) {
        super(file, charset);
    }

    /**
     * Construct a new ScriptFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public ScriptFile(File file, List<String> root) {
        super(file, root);
    }

    /**
     * Construct a new ScriptFile providing a file
     *
     * @param file the file to read from and write to
     */
    public ScriptFile(File file) {
        super(file);
    }

    /**
     * Run the script file in the current thread
     * <p><i>This is a blocking operation</i></p>
     *
     * @return the finished process
     * @throws IOException          thrown if something goes wrong
     * @throws InterruptedException thrown if the process gets interrupted
     */
    public Process run() throws IOException, InterruptedException {
        var process = runAsync();
        process.waitFor();
        return process;
    }

    /**
     * Run the script file asynchronously
     *
     * @return the live process
     * @throws IOException thrown if something goes wrong
     */
    public Process runAsync() throws IOException {
        var builder = new ProcessBuilder("bash", getFile().getAbsolutePath())
                .directory(getFile().getAbsoluteFile().getParentFile())
                .redirectOutput(redirect());
        var process = builder.start();
        process.onExit().thenAccept(finished -> {
            if (deletion().apply(finished)) delete();
        });
        return process;
    }

    @Override
    public ScriptFile save() {
        return (ScriptFile) super.save();
    }

    @FunctionalInterface
    public interface Deletion extends Function<Process, Boolean> {
        /**
         * Will always return true
         */
        Deletion ALWAYS = process -> true;
        /**
         * Will always return false
         */
        Deletion NEVER = ALWAYS.negate();
        /**
         * Returns true if the process finished successfully
         */
        Deletion ON_SUCCESS = process -> process.exitValue() == 0;
        /**
         * Returns true if the process did not finish successfully
         */
        Deletion ON_FAILURE = ON_SUCCESS.negate();

        /**
         * Applies this function to the given process
         *
         * @param process the finished process
         * @return whether to delete the file
         */
        @Override
        Boolean apply(Process process);

        /**
         * Negate the current statement
         *
         * @return the negated deletion statement
         */
        default Deletion negate() {
            return process -> !apply(process);
        }
    }
}

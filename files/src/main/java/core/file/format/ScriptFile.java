package core.file.format;

import core.io.PathIO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * The ScriptFile class represents a script file that can be executed.
 * It extends the TextFile class and includes functionality to run the script
 * both synchronously and asynchronously.
 */
@Getter
@Setter
@NullMarked
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
     * Construct a new ScriptFile providing a file, charset, and default root object
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public ScriptFile(PathIO io, Charset charset, List<String> root) {
        super(io, charset, root);
    }

    /**
     * Construct a new ScriptFile providing a file and charset
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    public ScriptFile(PathIO io, Charset charset) {
        super(io, charset);
    }

    /**
     * Construct a new ScriptFile providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    public ScriptFile(PathIO io, List<String> root) {
        super(io, root);
    }

    /**
     * Construct a new ScriptFile providing a file
     *
     * @param io the file to read from and write to
     */
    public ScriptFile(PathIO io) {
        super(io);
    }

    /**
     * Run the script file in the current thread
     * <p><i>This is a blocking operation</i></p>
     *
     * @return the finished process
     * @throws IOException          thrown if something goes wrong
     * @throws InterruptedException thrown if the process gets interrupted
     * @throws ExecutionException   thrown if the computation threw an exception
     * @see #runAsync()
     */
    public Process run() throws IOException, InterruptedException, ExecutionException {
        return runAsync().get();
    }

    /**
     * Run the script file asynchronously
     *
     * @return the live process
     * @throws IOException thrown if something goes wrong
     */
    @SuppressWarnings("UseOfProcessBuilder")
    public CompletableFuture<Process> runAsync() throws IOException {
        var builder = new ProcessBuilder("bash", getIO().getPath().toString())
                .directory(getIO().getPath().toFile())
                .redirectOutput(redirect());
        var process = builder.start();
        return process.onExit().thenApply(finished -> {
            try {
                if (!deletion().apply(finished)) return finished;
                getIO().delete();
                return finished;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * The Deletion interface provides a functional contract to determine whether
     * a file associated with a finished process should be deleted.
     * <p>
     * It extends the Function interface with a specific input of Process and a Boolean output.
     * <p>
     * The interface provides four predefined behaviors:
     *
     * <ul>
     * <li><b>ALWAYS</b>: always returns true.</li>
     * <li><b>NEVER</b>: always returns false.</li>
     * <li><b>ON_SUCCESS</b>: returns true if the process finished successfully.</li>
     * <li><b>ON_FAILURE</b>: returns true if the process didn't finish successfully.</li>
     * </ul>
     */
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
         * Returns true if the process didn't finish successfully
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

    /**
     * Retrieves the IO object associated with this ScriptFile.
     *
     * @return the PathIO instance being used by this ScriptFile
     */
    @Override
    public PathIO getIO() {
        return (PathIO) super.getIO();
    }
}

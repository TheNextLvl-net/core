package core.file.format;

import core.io.PathIO;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * The ScriptFile class represents a script file that can be executed.
 * It extends the TextFile class and includes functionality to run the script
 * both synchronously and asynchronously.
 */
@NullMarked
public class ScriptFile extends TextFile {
    private Deletion deletion = Deletion.NEVER;
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
                .directory(Optional.ofNullable(getIO().getPath().getParent())
                        .map(Path::toFile).orElseGet(() -> new File("/")))
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

    /**
     * Retrieves the Deletion strategy associated with this ScriptFile.
     *
     * @return the Deletion strategy that determines whether the file
     *         should be deleted under specific conditions
     */
    public Deletion deletion() {
        return deletion;
    }

    /**
     * Sets the deletion behavior for the file associated with this ScriptFile.
     *
     * @param deletion the Deletion strategy to determine whether the file should
     *                 be deleted under certain circumstances
     * @return the current ScriptFile instance with the updated deletion behavior
     */
    public ScriptFile deletion(Deletion deletion) {
        this.deletion = deletion;
        return this;
    }

    /**
     * Retrieves the {@link ProcessBuilder.Redirect} where the output of this script is redirected to
     *
     * @return the current redirect configuration
     */
    public ProcessBuilder.Redirect redirect() {
        return redirect;
    }

    /**
     * Sets the redirect configuration for the output of this script.
     *
     * @param redirect the {@link ProcessBuilder.Redirect} to set for the script's output
     * @return the current ScriptFile instance with the updated redirect configuration
     */
    public ScriptFile redirect(ProcessBuilder.Redirect redirect) {
        this.redirect = redirect;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ScriptFile that = (ScriptFile) o;
        return Objects.equals(deletion, that.deletion) && Objects.equals(redirect, that.redirect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), deletion, redirect);
    }

    @Override
    public String toString() {
        return "ScriptFile{" +
               "deletion=" + deletion +
               ", redirect=" + redirect +
               "} " + super.toString();
    }
}

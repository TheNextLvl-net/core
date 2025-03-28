package core.file;

import core.io.IO;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.FileAttribute;
import java.util.Objects;

/**
 * Abstract class for performing file input and output operations.
 *
 * @param <R> the type of the root object
 */
public abstract class FileIO<R> {
    private final IO io;
    private Charset charset;
    private @Nullable R root;

    private boolean loaded;

    /**
     * Construct a new FileIO providing a file, charset, and default root object
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */

    protected FileIO(@NonNull IO io, @NonNull Charset charset, @Nullable R root) {
        this.io = io;
        this.charset = charset;
        this.root = root;
        this.loaded = !getIO().exists();
    }

    /**
     * Construct a new FileIO providing a file and charset
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     */

    protected FileIO(@NonNull IO io, @NonNull Charset charset) {
        this(io, charset, null);
    }

    /**
     * Construct a new FileIO providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    protected FileIO(@NonNull IO io, R root) {
        this(io, StandardCharsets.UTF_8, root);
    }

    /**
     * Construct a new FileIO providing a file
     *
     * @param io the file to read from and write to
     */
    protected FileIO(@NonNull IO io) {
        this(io, (R) null);
    }

    /**
     * Sets the root object for this FileIO instance.
     *
     * @param root the new root object to be set
     * @return the FileIO instance with the updated root
     */
    public final @NonNull FileIO<R> setRoot(R root) {
        this.loaded = true;
        this.root = root;
        return this;
    }

    /**
     * Retrieves the root object for this instance.
     * If the root object is not already loaded,
     * it loads the root object by calling the abstract `load` method.
     *
     * @return the root object of type R
     */
    public R getRoot() {
        if (loaded) return root;
        loaded = true;
        return root = load();
    }

    /**
     * Load the content from the file
     *
     * @return the file content
     */
    protected abstract R load();

    /**
     * Save the root object to the file
     *
     * @param attributes the file attributes
     * @return the own instance
     */
    public abstract @NonNull FileIO<R> save(@NonNull FileAttribute<?>... attributes);

    /**
     * Reload the current instance<br>
     * <i>Unsaved changes will be lost</i>
     *
     * @return the file content
     */
    public @NonNull FileIO<R> reload() {
        return setRoot(load());
    }

    /**
     * Save the file if it doesn't exist
     *
     * @return the own instance
     */
    public @NonNull FileIO<R> saveIfAbsent() {
        return getIO().exists() ? this : save();
    }

    /**
     * Deletes the file associated with this FileIO instance.
     *
     * @return whether the file was successfully deleted
     * @throws IOException if an I/O error occurs
     */
    public boolean delete() throws IOException {
        return getIO().delete();
    }

    /**
     * Retrieves the IO instance associated with this FileIO.
     *
     * @return the IO instance that provides input and output operations.
     */
    public IO getIO() {
        return io;
    }

    /**
     * Retrieves the charset used by this instance for reading and writing operations.
     *
     * @return the charset used for I/O operations
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Sets the charset to be used for reading and writing operations in this FileIO instance.
     *
     * @param charset the Charset to be used for reading and writing operations
     * @return the current FileIO instance with the updated Charset
     */
    public FileIO<R> setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FileIO<?> fileIO = (FileIO<?>) o;
        return Objects.equals(io, fileIO.io) && Objects.equals(charset, fileIO.charset) && Objects.equals(root, fileIO.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(io, charset, root);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
               "io=" + io +
               ", charset=" + charset +
               ", root=" + root +
               ", loaded=" + loaded +
               '}';
    }
}

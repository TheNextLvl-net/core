package core.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

/**
 * Abstract class for performing file input and output operations.
 *
 * @param <R> the type of the root object
 */
public abstract class FileIO<R> {
    private final Path file;
    private final Charset charset;
    
    private R root;
    private boolean loaded;

    /**
     * Construct a new FileIO providing a file, charset, and default root object
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    protected FileIO(Path file, Charset charset, R root) {
        this.file = file;
        this.charset = charset;
        this.root = root;
    }

    /**
     * Construct a new FileIO providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    protected FileIO(Path file, R root) {
        this(file, StandardCharsets.UTF_8, root);
    }

    /**
     * Sets the root object for this FileIO instance.
     *
     * @param root the new root object to be set
     * @return the FileIO instance with the updated root
     */
    public final FileIO<R> setRoot(R root) {
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
    public abstract FileIO<R> save(FileAttribute<?>... attributes);

    /**
     * Reload the current instance<br>
     * <i>Unsaved changes will be lost</i>
     *
     * @return the file content
     */
    public FileIO<R> reload() {
        return setRoot(load());
    }

    /**
     * Save the file if it doesn't exist
     *
     * @return the own instance
     */
    public FileIO<R> saveIfAbsent() {
        return Files.isRegularFile(file) ? this : save();
    }

    /**
     * Deletes the file associated with this FileIO instance.
     *
     * @return whether the file was successfully deleted
     * @throws IOException if an I/O error occurs
     */
    public boolean delete() throws IOException {
        return Files.deleteIfExists(file);
    }

    /**
     * Retrieves the IO instance associated with this FileIO.
     *
     * @return the IO instance that provides input and output operations.
     */
    public Path getFile() {
        return file;
    }

    /**
     * Retrieves the charset used by this instance for reading and writing operations.
     *
     * @return the charset used for I/O operations
     */
    public Charset getCharset() {
        return charset;
    }
}

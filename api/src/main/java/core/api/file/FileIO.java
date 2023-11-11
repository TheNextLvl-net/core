package core.api.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Getter
@ToString
@EqualsAndHashCode
public abstract class FileIO<R, T extends FileIO<R, T>> {
    private final @NotNull File file;
    private @NotNull Charset charset;
    private R root;

    /**
     * Construct a new FileIO providing a file, charset and default root object
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    protected FileIO(@NotNull File file, @NotNull Charset charset, R root) {
        this.file = file;
        this.charset = charset;
        this.root = root;
    }

    /**
     * Construct a new FileIO providing a file and charset
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    protected FileIO(@NotNull File file, @NotNull Charset charset) {
        this(file, charset, null);
    }

    /**
     * Construct a new FileIO providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    protected FileIO(@NotNull File file, R root) {
        this(file, StandardCharsets.UTF_8, root);
    }

    /**
     * Construct a new FileIO providing a file
     *
     * @param file the file to read from and write to
     */
    protected FileIO(@NotNull File file) {
        this(file, (R) null);
    }

    public T setRoot(R root) {
        this.root = root;
        return (T) this;
    }

    public T setCharset(Charset charset) {
        this.charset = charset;
        return (T) this;
    }

    /**
     * Load the content from the file
     *
     * @return the file content
     */
    protected R load() {
        return load(getFile());
    }

    /**
     * Load the content from the file
     *
     * @return the file content
     */
    protected abstract R load(File file);

    /**
     * Save the root object to the file
     *
     * @param file the file to save to
     * @return the own instance
     */
    public abstract T save(File file);

    /**
     * Save the root object to the file
     *
     * @return the own instance
     */
    public T save() {
        return save(getFile());
    }

    /**
     * Reload the current instance from the file<br>
     * <i>The current state will be lost</i>
     *
     * @param file the file to read from
     * @return the file content
     */
    public T reload(File file) {
        return setRoot(load(file));
    }

    /**
     * Reload the current instance <br>
     * <i>The current state will be lost</i>
     *
     * @return the file content
     */
    public T reload() {
        return reload(getFile());
    }

    /**
     * Save the file if it does not exist
     *
     * @return the own instance
     */
    public T saveIfAbsent() {
        return exists() ? (T) this : save();
    }

    /**
     * Get whether the file exists
     *
     * @return true if the file exists
     */
    public boolean exists() {
        return exists(getFile());
    }

    /**
     * Get the name of the file
     *
     * @return the file name
     */
    public String getName() {
        return getFile().getName();
    }

    /**
     * Delete the file
     *
     * @return whether the file was successfully deleted
     */
    public boolean delete() {
        return getFile().delete();
    }

    /**
     * Get whether the given file exists
     *
     * @return true if the file exists
     */
    protected static boolean exists(File file) {
        return file.exists();
    }

    /**
     * Create the file and its parent directories
     *
     * @throws IOException thrown if something goes wrong
     */
    protected static void createFile(File file) throws IOException {
        file.getAbsoluteFile().getParentFile().mkdirs();
        file.getAbsoluteFile().createNewFile();
    }
}

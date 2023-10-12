package core.api.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public abstract class FileIO<R> {
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

    /**
     * Load the content from the file
     *
     * @return the file content
     */
    public abstract R load();

    /**
     * Save the root object to the file
     *
     * @return the own instance
     */
    public abstract FileIO<R> save();

    /**
     * Save the file if it does not exist
     *
     * @return the own instance
     */
    public FileIO<R> saveIfAbsent() {
        return exists() ? this : save();
    }

    /**
     * Get whether the file exists
     *
     * @return true if the file exists
     */
    public boolean exists() {
        return getFile().exists();
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
     * Create the file and its parent directories
     *
     * @throws IOException thrown if something goes wrong
     */
    protected void createFile() throws IOException {
        var file = getFile().getAbsoluteFile();
        file.getParentFile().mkdirs();
        file.createNewFile();
    }
}

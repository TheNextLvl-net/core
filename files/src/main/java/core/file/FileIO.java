package core.file;

import core.io.IO;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.FileAttribute;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public abstract class FileIO<R> {
    private final @NotNull IO IO;
    private @NotNull Charset charset;
    private R root;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private boolean loaded;

    /**
     * Construct a new FileIO providing a file, charset and default root object
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    protected FileIO(@NotNull IO io, @NotNull Charset charset, R root) {
        this.IO = io;
        this.charset = charset;
        this.root = root;
    }

    /**
     * Construct a new FileIO providing a file and charset
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    protected FileIO(@NotNull IO io, @NotNull Charset charset) {
        this(io, charset, null);
    }

    /**
     * Construct a new FileIO providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    protected FileIO(@NotNull IO io, R root) {
        this(io, StandardCharsets.UTF_8, root);
    }

    /**
     * Construct a new FileIO providing a file
     *
     * @param io the file to read from and write to
     */
    protected FileIO(@NotNull IO io) {
        this(io, (R) null);
    }

    public final FileIO<R> setRoot(R root) {
        this.loaded = true;
        this.root = root;
        return this;
    }

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
     * Save the file if it does not exist
     *
     * @return the own instance
     */
    public FileIO<R> saveIfAbsent() {
        return getIO().exists() ? this : save();
    }

    /**
     * Delete the file
     *
     * @return whether the file was successfully deleted
     */
    public boolean delete() throws IOException {
        return getIO().delete();
    }
}

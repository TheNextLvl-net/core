package core.io;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Objects;

/**
 * PathIO is a concrete implementation of the IO interface that provides
 * Input/Output operations on file paths using the Java NIO (New I/O) API.
 */
@NullMarked
public class PathIO implements IO {
    private final Path path;

    PathIO(Path path) {
        this.path = path;
    }

    @Override
    public InputStream inputStream(OpenOption... options) throws IOException {
        return Files.newInputStream(path, options);
    }

    @Override
    public OutputStream outputStream(OpenOption... options) throws IOException {
        return Files.newOutputStream(path, options);
    }

    @Override
    public boolean createParents(FileAttribute<?>... attributes) throws IOException {
        var parent = path.toAbsolutePath().getParent();
        Files.createDirectories(parent, attributes);
        return true;
    }

    @Override
    public boolean exists(LinkOption... options) {
        return Files.exists(path, options);
    }

    @Override
    public boolean delete() throws IOException {
        return Files.deleteIfExists(path);
    }

    /**
     * Returns the path associated with this {@code PathIO} instance.
     *
     * @return the {@code Path} representing the file or directory managed by this object
     */
    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return path.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathIO pathIO)) return false;
        return Objects.equals(path, pathIO.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }
}

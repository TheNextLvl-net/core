package core.io;


import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

/**
 * Interface defining basic Input/Output operations.
 */
@NullMarked
public interface IO {
    /**
     * Get the OutputStream of this IO
     *
     * @param options the options that should be applied
     * @return an input stream
     * @throws IOException thrown if something goes wrong
     */
    InputStream inputStream(OpenOption... options) throws IOException;

    /**
     * Get the OutputStream of this IO
     *
     * @param options the options that should be applied
     * @return an output stream
     * @throws IOException thrown if something goes wrong
     */
    OutputStream outputStream(OpenOption... options) throws IOException;

    /**
     * Create the file and its parent directories
     *
     * @param attributes the standard attributes to define
     * @return whether the parent directories were successfully created
     * @throws IOException thrown if something goes wrong
     */
    boolean createParents(FileAttribute<?>... attributes) throws IOException;

    /**
     * Get whether the given file exists
     *
     * @param options the link options
     * @return true if the file exists
     */
    boolean exists(LinkOption... options);

    /**
     * Delete the file
     *
     * @return whether the file was successfully deleted
     * @throws IOException thrown if something goes wrong
     */
    boolean delete() throws IOException;

    /**
     * Creates a PathIO instance from a given URI.
     *
     * @param uri the URI to be converted to a PathIO instance
     * @return a new PathIO instance representing the given URI
     */
    static PathIO of(URI uri) {
        return of(Path.of(uri));
    }

    /**
     * Creates a PathIO instance based on the specified path strings.
     *
     * @param first the first part of the path
     * @param more  additional parts of the path
     * @return a new PathIO instance representing the given path
     */
    static PathIO of(String first, String... more) {
        return of(Path.of(first, more));
    }

    /**
     * Creates a PathIO instance from a given parent File and child path string.
     *
     * @param parent the parent directory as a File object
     * @param child  the child path string
     * @return a new PathIO instance representing the given path
     */
    static PathIO of(File parent, String child) {
        return of(new File(parent, child));
    }

    /**
     * Creates a PathIO instance from a given File object.
     *
     * @param file the File object to be converted to a PathIO instance
     * @return a new PathIO instance representing the given File
     */
    static PathIO of(File file) {
        return of(file.toPath());
    }

    /**
     * Creates a PathIO instance from a given Path.
     *
     * @param path the Path to be converted to a PathIO instance
     * @return a new PathIO instance representing the given Path
     */
    static PathIO of(Path path) {
        return new PathIO(path);
    }

    /**
     * Creates a StreamIO instance from a given resource.
     *
     * @param resource the resource name to be loaded as InputStream
     * @return a new StreamIO instance with the InputStream from the specified resource
     */
    static StreamIO ofResource(String resource) {
        return of(IO.class.getClassLoader().getResourceAsStream(resource), null);
    }

    /**
     * Creates a StreamIO instance from the specified input and output streams.
     *
     * @param inputStream  the InputStream
     * @param outputStream the OutputStream
     * @return a new StreamIO instance with the provided InputStream and OutputStream
     * @throws IllegalArgumentException if both inputStream and outputStream are null
     */
    static StreamIO of(@Nullable InputStream inputStream, @Nullable OutputStream outputStream) {
        if (inputStream == null && outputStream == null)
            throw new IllegalArgumentException("In & Out can't be both null");
        return new StreamIO(inputStream, outputStream);
    }
}

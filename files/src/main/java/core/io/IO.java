package core.io;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

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

    static PathIO of(URI uri) {
        return of(Path.of(uri));
    }

    static PathIO of(String first, String... more) {
        return of(Path.of(first, more));
    }

    static PathIO of(File parent, String child) {
        return of(new File(parent, child));
    }

    static PathIO of(File file) {
        return of(file.toPath());
    }

    static PathIO of(Path path) {
        return new PathIO(path);
    }

    static StreamIO ofResource(String resource) {
        return of(IO.class.getClassLoader().getResourceAsStream(resource), null);
    }

    static StreamIO of(@Nullable InputStream inputStream, @Nullable OutputStream outputStream) {
        if (inputStream == null && outputStream == null)
            throw new IllegalArgumentException("In & Out can't be both null");
        return new StreamIO(inputStream, outputStream);
    }
}

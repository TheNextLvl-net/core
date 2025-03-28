package core.io;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;

/**
 * A wrapper class for handling InputStream and OutputStream.
 * Allows checking for readability and writability
 * and provides implementations for the IO interface methods.
 */
@NullMarked
public class StreamIO implements IO, AutoCloseable {
    private final @Nullable InputStream inputStream;
    private final @Nullable OutputStream outputStream;

    StreamIO(@Nullable InputStream inputStream, @Nullable OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    /**
     * Checks if the input stream is readable.
     *
     * @return true if the input stream is not null and therefore readable, false otherwise
     */
    public boolean isReadable() {
        return inputStream != null;
    }

    /**
     * Checks if the output stream is writable.
     *
     * @return true if the output stream is not null and therefore writable, false otherwise
     */
    public boolean isWritable() {
        return outputStream != null;
    }

    @Override
    public InputStream inputStream(OpenOption... options) {
        if (inputStream != null) return inputStream;
        throw new IllegalStateException("This object is write only");
    }

    @Override
    public OutputStream outputStream(OpenOption... options) {
        if (outputStream != null) return outputStream;
        throw new IllegalStateException("This object is read only");
    }

    @Override
    public boolean createParents(FileAttribute<?>... attributes) {
        return true;
    }

    @Override
    public boolean exists(LinkOption... options) {
        return true;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) inputStream.close();
        if (outputStream != null) outputStream.close();
    }
}

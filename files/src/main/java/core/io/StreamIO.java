package core.io;

import core.annotation.FieldsAreNullableByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;

@FieldsAreNullableByDefault
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class StreamIO implements IO, AutoCloseable {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public boolean isReadable() {
        return inputStream != null;
    }

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

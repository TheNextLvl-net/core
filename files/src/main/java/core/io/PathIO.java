package core.io;

import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Objects;

@Getter
@FieldsAreNotNullByDefault
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PathIO implements IO {
    private final Path path;

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

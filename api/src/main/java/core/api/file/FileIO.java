package core.api.file;

import core.annotation.MethodsReturnNonnullByDefault;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Setter
@Getter
@Accessors(chain = true)
@MethodsReturnNonnullByDefault
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class FileIO<R> {
    private final File file;
    private Charset charset;
    private R root;

    protected FileIO(File file, R root) {
        this(file, StandardCharsets.UTF_8, root);
    }

    protected FileIO(File file) {
        this(file, null);
        setRoot(load());
    }

    public abstract R load();

    public abstract void save();

    public boolean delete() {
        return getFile().delete();
    }

    protected void createFile() throws IOException {
        var file = getFile().getAbsoluteFile();
        file.getParentFile().mkdirs();
        file.createNewFile();
    }
}

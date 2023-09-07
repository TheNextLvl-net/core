package core.api.file.format;

import core.api.file.FileIO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TextFile extends FileIO<List<String>> {

    /**
     * Construct a new TextFile providing a file, charset and default root object
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public TextFile(File file, Charset charset, List<String> root) {
        super(file, charset, root);
    }

    /**
     * Construct a new TextFile providing a file and charset
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    public TextFile(File file, Charset charset) {
        super(file, charset);
    }

    /**
     * Construct a new TextFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public TextFile(File file, List<String> root) {
        super(file, root);
    }

    /**
     * Construct a new TextFile providing a file
     *
     * @param file the file to read from and write to
     */
    public TextFile(File file) {
        super(file, new ArrayList<>());
    }

    @Override
    public List<String> load() {
        try {
            if (!getFile().exists()) return getRoot();
            return Files.readAllLines(getFile().toPath(), getCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TextFile save() {
        try {
            createFile();
            Files.writeString(getFile().toPath(), String.join("\n", getRoot()), getCharset());
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

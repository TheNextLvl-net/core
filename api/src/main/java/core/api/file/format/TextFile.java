package core.api.file.format;

import core.api.file.FileIO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
        setRoot(load());
    }

    /**
     * Construct a new TextFile providing a file and charset
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    public TextFile(File file, Charset charset) {
        this(file, charset, new ArrayList<>());
    }

    /**
     * Construct a new TextFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public TextFile(File file, List<String> root) {
        this(file, StandardCharsets.UTF_8, root);
    }

    /**
     * Construct a new TextFile providing a file
     *
     * @param file the file to read from and write to
     */
    public TextFile(File file) {
        this(file, StandardCharsets.UTF_8);
    }

    @Override
    protected List<String> load(File file) {
        try {
            if (!exists(file)) return getRoot();
            return Files.readAllLines(file.toPath(), getCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TextFile save(File file) {
        try {
            createFile(file);
            Files.writeString(file.toPath(), String.join("\n", getRoot()), getCharset());
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TextFile save() {
        return (TextFile) super.save();
    }

    @Override
    public TextFile saveIfAbsent() {
        return (TextFile) super.saveIfAbsent();
    }
}

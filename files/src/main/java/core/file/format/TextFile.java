package core.file.format;

import core.file.FileIO;
import core.io.IO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.*;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TextFile extends FileIO<List<String>> {

    /**
     * Construct a new TextFile providing a file, charset and default root object
     *
     * @param io the file to read from and write to
     * @param charset  the charset to use for read and write operations
     * @param root     the default root object
     */
    public TextFile(IO io, Charset charset, List<String> root) {
        super(io, charset, root);
    }

    /**
     * Construct a new TextFile providing a file and charset
     *
     * @param io the file to read from and write to
     * @param charset  the charset to use for read and write operations
     */
    public TextFile(IO io, Charset charset) {
        this(io, charset, new ArrayList<>());
    }

    /**
     * Construct a new TextFile providing a file and default root object
     *
     * @param io the file to read from and write to
     * @param root     the default root object
     */
    public TextFile(IO io, List<String> root) {
        this(io, StandardCharsets.UTF_8, root);
    }

    /**
     * Construct a new TextFile providing a file
     *
     * @param io the file to read from and write to
     */
    public TextFile(IO io) {
        this(io, StandardCharsets.UTF_8);
    }

    @Override
    protected List<String> load() {
        try {
            if (!getIO().exists()) return getRoot();
            try (var reader = new BufferedReader(new InputStreamReader(
                    getIO().inputStream(READ),
                    getCharset()
            ))) {
                return reader.lines().collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TextFile save(FileAttribute<?>... attributes) {
        try {
            getIO().createParents(attributes);
            try (var writer = new BufferedWriter(new OutputStreamWriter(
                    getIO().outputStream(WRITE, CREATE, TRUNCATE_EXISTING),
                    getCharset()
            ))) {
                writer.write(String.join("\n", getRoot()));
                return this;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

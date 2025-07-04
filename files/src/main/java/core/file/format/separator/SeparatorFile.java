package core.file.format.separator;

import core.file.FileIO;
import core.io.IO;
import org.jspecify.annotations.NullMarked;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.*;

/**
 * An abstract class for handling files with separated values, such as CSV or TSV files.
 * Provides methods for inserting, removing, and selecting rows based on given parameters.
 * Extends the {@link FileIO} class and implements its `load` and `save` methods.
 */
@NullMarked
public abstract class SeparatorFile extends FileIO<List<List<String>>> {
    /**
     * Construct a new SeparatorFile providing a file, charset, and default root object
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    protected SeparatorFile(IO io, Charset charset, List<List<String>> root) {
        super(io, charset, root);
    }

    /**
     * Construct a new SeparatorFile providing a file and charset
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    protected SeparatorFile(IO io, Charset charset) {
        this(io, charset, new ArrayList<>());
    }

    /**
     * Construct a new SeparatorFile providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    protected SeparatorFile(IO io, List<List<String>> root) {
        this(io, StandardCharsets.UTF_8, root);
    }

    /**
     * Construct a new SeparatorFile providing a file
     *
     * @param io the file to read from and write to
     */
    protected SeparatorFile(IO io) {
        this(io, new ArrayList<>());
    }

    /**
     * Insert an array of values
     *
     * @param values the values to insert
     */
    public void insert(Object... values) {
        var insert = new ArrayList<String>();
        for (var o : values) insert.add(o.toString());
        getRoot().add(insert);
    }

    /**
     * Remove an array of values
     *
     * @param values the values to remove
     * @return whether any element was removed
     */
    public boolean remove(Object... values) {
        return getRoot().removeIf(entry -> {
            for (var object : values) if (!entry.contains(object.toString())) return false;
            return true;
        });
    }

    /**
     * Select every row matching the given parameters
     *
     * @param parameters the known parameters
     * @return the matching rows
     */
    public List<List<String>> select(Object... parameters) {
        var matches = new ArrayList<List<String>>();
        rows:
        for (var row : getRoot()) {
            for (var parameter : parameters) if (!row.contains(parameter.toString())) continue rows;
            matches.add(row);
        }
        return matches;
    }

    @Override
    protected List<List<String>> load() {
        try {
            if (!getIO().exists()) return getRoot();
            try (var reader = new BufferedReader(new InputStreamReader(
                    getIO().inputStream(READ),
                    getCharset()
            ))) {
                return reader.lines()
                        .filter(s -> !s.isBlank())
                        .map(s -> List.of(s.split(getDelimiter())))
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @NullMarked
    public FileIO<List<List<String>>> save(FileAttribute<?>... attributes) {
        try {
            var root = getRoot();
            getIO().createParents(attributes);
            try (var writer = new BufferedWriter(new OutputStreamWriter(
                    getIO().outputStream(WRITE, CREATE, TRUNCATE_EXISTING),
                    getCharset()
            ))) {
                writer.write(String.join("\n", root.stream()
                        .map(strings -> String.join(getDelimiter(), strings))
                        .toList()));
                return this;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the delimiter used to separate values
     *
     * @return the delimiter
     */
    public abstract String getDelimiter();
}

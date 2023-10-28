package core.api.file.format.separator;

import core.api.file.FileIO;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public abstract class SeparatorFile extends FileIO<List<List<String>>> {

    /**
     * Construct a new SeparatorFile providing a file, charset and default root object
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    protected SeparatorFile(File file, Charset charset, List<List<String>> root) {
        super(file, charset, root);
        setRoot(load());
    }

    /**
     * Construct a new SeparatorFile providing a file and charset
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    protected SeparatorFile(File file, Charset charset) {
        this(file, charset, new ArrayList<>());
    }

    /**
     * Construct a new SeparatorFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    protected SeparatorFile(File file, List<List<String>> root) {
        this(file, StandardCharsets.UTF_8, root);
    }

    /**
     * Construct a new SeparatorFile providing a file
     *
     * @param file the file to read from and write to
     */
    protected SeparatorFile(File file) {
        this(file, new ArrayList<>());
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
    public List<List<String>> load() {
        try {
            if (!getFile().exists()) return getRoot();
            var content = new ArrayList<List<String>>();
            Files.readAllLines(getFile().toPath(), getCharset()).forEach(s -> {
                if (!s.isBlank()) content.add(List.of(s.split(getDelimiter())));
            });
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SeparatorFile save() {
        try {
            createFile();
            var root = String.join("\n", getRoot().stream().map(strings ->
                    String.join(getDelimiter(), strings)).toList()) + "\n";
            Files.writeString(file.toPath(), root, getCharset());
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SeparatorFile saveIfAbsent() {
        return (SeparatorFile) super.saveIfAbsent();
    }

    public abstract String getDelimiter();
}

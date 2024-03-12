package core.file.format;

import core.file.FileIO;
import core.file.Validatable;
import core.io.IO;
import core.util.Properties;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.FileAttribute;

import static java.nio.file.StandardOpenOption.*;

public class PropertiesFile extends FileIO<Properties> implements Validatable<Properties> {
    protected final Properties defaultRoot;

    /**
     * Construct a new PropertiesFile providing a file, charset and default root object
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public PropertiesFile(IO io, Charset charset, Properties root) {
        super(io, charset, root);
        defaultRoot = root;
    }

    /**
     * Construct a new PropertiesFile providing a file and charset
     *
     * @param io      the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    public PropertiesFile(IO io, Charset charset) {
        this(io, charset, new Properties());
    }

    /**
     * Construct a new PropertiesFile providing a file and default root object
     *
     * @param io   the file to read from and write to
     * @param root the default root object
     */
    public PropertiesFile(IO io, Properties root) {
        this(io, StandardCharsets.UTF_8, root);
    }

    /**
     * Construct a new PropertiesFile providing a file
     *
     * @param io the file to read from and write to
     */
    public PropertiesFile(IO io) {
        this(io, StandardCharsets.UTF_8);
    }

    @Override
    protected Properties load() {
        try {
            if (!getIO().exists()) return getRoot();
            return new Properties().read(getIO().inputStream(READ), getCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileIO<Properties> save(FileAttribute<?>... attributes) {
        try {
            getIO().createParents(attributes);
            try (var writer = new BufferedWriter(new OutputStreamWriter(
                    getIO().outputStream(WRITE, CREATE, TRUNCATE_EXISTING),
                    getCharset()
            ))) {
                getRoot().store(writer, null);
                return this;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileIO<Properties> validate(Scope scope) {
        if (!getIO().exists()) return this;
        if (scope.isFiltering()) filterUnused(defaultRoot, getRoot());
        if (scope.isFilling()) fillMissing(defaultRoot, getRoot());
        return this;
    }

    private static Properties fillMissing(Properties defaultRoot, Properties currentRoot) {
        currentRoot.merge(defaultRoot);
        return currentRoot;
    }

    private static Properties filterUnused(Properties defaultRoot, Properties currentRoot) {
        currentRoot.removeIf(entry -> !defaultRoot.containsKey(entry.getKey()));
        return currentRoot;
    }
}

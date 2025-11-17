package core.file.format;

import core.file.FileIO;
import core.file.Validatable;
import core.io.IO;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.FileAttribute;
import java.util.Properties;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * Represents a properties file and provides methods for reading, writing, and validating the properties file.
 * This class extends {@link FileIO} to handle file input and output operations
 * with properties files and implements {@link Validatable} to validate the properties.
 */
@NullMarked
public class PropertiesFile extends FileIO<@NonNull Properties> implements Validatable<@NonNull Properties> {
    /**
     * The default set of properties to be used as the base configuration.
     * These properties will be referenced when no specific properties are provided or found.
     */
    protected final Properties defaultRoot;

    /**
     * Construct a new PropertiesFile providing a file, charset, and default root object
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
        if (!getIO().exists()) return (Properties) getRoot().clone();
        try (var reader = new InputStreamReader(getIO().inputStream(READ), getCharset());
             var buffer = new BufferedReader(reader)) {
            var properties = new Properties();
            properties.load(buffer);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileIO<Properties> save(FileAttribute<?>... attributes) {
        try {
            var root = getRoot();
            getIO().createParents(attributes);
            try (var writer = new BufferedWriter(new OutputStreamWriter(
                    getIO().outputStream(WRITE, CREATE, TRUNCATE_EXISTING),
                    getCharset()
            ))) {
                root.store(writer, null);
                return this;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PropertiesFile validate(Scope scope) {
        var root = getRoot();
        if (root == defaultRoot) return this;
        if (scope.isFiltering()) root.entrySet().removeIf(entry ->
                !defaultRoot.containsKey(entry.getKey()));
        if (scope.isFilling()) merge(defaultRoot);
        return this;
    }

    public PropertiesFile merge(Properties properties) {
        var root = getRoot();
        properties.forEach((key, value) -> {
            if (root.containsKey(key)) return;
            root.put(key, value);
        });
        return this;
    }
}

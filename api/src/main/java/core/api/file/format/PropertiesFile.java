package core.api.file.format;

import core.api.file.FileIO;
import core.api.file.Validatable;
import core.util.Properties;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class PropertiesFile extends FileIO<Properties, PropertiesFile> implements Validatable<PropertiesFile> {
    protected final Properties defaultRoot;

    /**
     * Construct a new PropertiesFile providing a file, charset and default root object
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public PropertiesFile(File file, Charset charset, Properties root) {
        super(file, charset, root);
        defaultRoot = root;
        setRoot(load());
    }

    /**
     * Construct a new PropertiesFile providing a file and charset
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    public PropertiesFile(File file, Charset charset) {
        this(file, charset, Properties.ordered());
    }

    /**
     * Construct a new PropertiesFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public PropertiesFile(File file, Properties root) {
        this(file, StandardCharsets.UTF_8, root);
    }

    /**
     * Construct a new PropertiesFile providing a file
     *
     * @param file the file to read from and write to
     */
    public PropertiesFile(File file) {
        this(file, StandardCharsets.UTF_8);
    }

    @Override
    protected Properties load(File file) {
        if (!exists(file)) return getRoot();
        try (var reader = Files.newBufferedReader(file.toPath(), getCharset())) {
            return Properties.unordered().read(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PropertiesFile save(File file) {
        try {
            createFile(file);
            try (var writer = Files.newBufferedWriter(file.toPath(), getCharset())) {
                for (var comment : getRoot().comments()) writer.write("# %s%n".formatted(comment));
                var iterator = getRoot().map().entrySet().iterator();
                while (iterator.hasNext()) {
                    var entry = iterator.next();
                    writer.write(entry.getKey() + "=" + entry.getValue());
                    if (iterator.hasNext()) writer.newLine();
                }
            }
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PropertiesFile validate(Scope scope) {
        if (!exists()) return this;
        if (scope.isFiltering()) filterUnused(defaultRoot, getRoot());
        if (scope.isFilling()) fillMissing(defaultRoot, getRoot());
        return this;
    }

    private static Properties fillMissing(Properties defaultRoot, Properties currentRoot) {
        currentRoot.merge(defaultRoot);
        return currentRoot;
    }

    private static Properties filterUnused(Properties defaultRoot, Properties currentRoot) {
        currentRoot.removeIf((key, value) -> !defaultRoot.has(key));
        return currentRoot;
    }
}

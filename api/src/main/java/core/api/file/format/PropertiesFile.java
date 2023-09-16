package core.api.file.format;

import core.api.file.FileIO;
import core.util.Properties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class PropertiesFile extends FileIO<Properties> {

    /**
     * Construct a new PropertiesFile providing a file, charset and default root object
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     * @param root    the default root object
     */
    public PropertiesFile(File file, Charset charset, Properties root) {
        super(file, charset, root);
        setRoot(load());
    }

    /**
     * Construct a new PropertiesFile providing a file and charset
     *
     * @param file    the file to read from and write to
     * @param charset the charset to use for read and write operations
     */
    public PropertiesFile(File file, Charset charset) {
        this(file, charset, new Properties());
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
    public Properties load() {
        try {
            if (!getFile().exists()) return getRoot();
            var properties = new Properties();
            Files.readAllLines(getFile().toPath(), getCharset()).forEach(line -> {
                if (!line.strip().startsWith("#")) {
                    List<String> split = Arrays.asList(line.split("="));
                    if (split.isEmpty() || split.get(0).isEmpty()) return;
                    properties.set(split.get(0).stripIndent(), String.join("=", split.subList(1, split.size())));
                } else properties.addComment(line.substring(1).stripIndent());
            });
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PropertiesFile save() {
        try {
            createFile();
            try (var writer = Files.newBufferedWriter(getFile().toPath(), getCharset())) {
                for (var comment : getRoot().getComments()) writer.write("# %s%n".formatted(comment));
                for (var key : getRoot().getEntrieMap().keySet()) {
                    if (getRoot().has(key)) writer.write("%s=%s%n".formatted(key, getRoot().getString(key)));
                }
            }
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

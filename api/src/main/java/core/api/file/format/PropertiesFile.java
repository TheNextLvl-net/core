package core.api.file.format;

import core.api.file.FileIO;
import core.util.Properties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class PropertiesFile extends FileIO<Properties> {

    public PropertiesFile(File file) {
        super(file);
    }

    public PropertiesFile(String file) {
        this(new File(file));
    }

    public PropertiesFile(File parent, String child) {
        this(new File(parent, child));
    }

    public PropertiesFile(String parent, String child) {
        this(new File(parent, child));
    }

    @Override
    public Properties load() {
        Properties properties = new Properties();
        if (!getFile().exists()) return properties;
        try {
            for (String line : Files.readAllLines(getFile().toPath(), getCharset())) {
                if (!line.strip().startsWith("#")) {
                    List<String> split = Arrays.asList(line.split("="));
                    if (split.isEmpty() || split.get(0).isEmpty()) continue;
                    properties.set(split.get(0).stripIndent(), String.join("=", split.subList(1, split.size())));
                } else properties.addComment(line.substring(1).stripIndent());
            }
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            createFile();
            try (var writer = Files.newBufferedWriter(getFile().toPath(), getCharset())) {
                for (String comment : getRoot().getComments()) writer.write("# %s%n".formatted(comment));
                for (String key : getRoot().getEntrieMap().keySet()) {
                    if (getRoot().has(key)) writer.write("%s=%s%n".formatted(key, getRoot().getString(key)));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

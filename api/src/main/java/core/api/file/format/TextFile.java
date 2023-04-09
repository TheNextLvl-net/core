package core.api.file.format;

import core.api.file.FileIO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class TextFile extends FileIO<List<String>> {

    public TextFile(File file) {
        super(file);
    }

    public TextFile(String file) {
        this(new File(file));
    }

    public TextFile(File parent, String child) {
        this(new File(parent, child));
    }

    public TextFile(String parent, String child) {
        this(new File(parent, child));
    }

    @Override
    public List<String> load() {
        try {
            if (!getFile().exists()) return new ArrayList<>();
            return Files.readAllLines(getFile().toPath(), getCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            createFile();
            Files.writeString(getFile().toPath(), String.join("\n", getRoot()), getCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

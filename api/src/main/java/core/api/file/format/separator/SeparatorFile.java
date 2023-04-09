package core.api.file.format.separator;

import core.api.file.FileIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public abstract class SeparatorFile extends FileIO<List<List<String>>> {

    public SeparatorFile(File file) {
        super(file);
    }

    public SeparatorFile(String file) {
        this(new File(file));
    }

    public SeparatorFile(File parent, String child) {
        this(new File(parent, child));
    }

    public SeparatorFile(String parent, String child) {
        this(new File(parent, child));
    }

    public void insert(Object... insertion) {
        List<String> insert = new ArrayList<>();
        for (Object o : insertion) insert.add(o.toString());
        getRoot().add(insert);
    }

    public boolean remove(Object... objects) {
        return getRoot().removeIf(entry -> {
            for (Object object : objects) if (!entry.contains(object.toString())) return false;
            return true;
        });
    }

    public List<List<String>> select(Object... objects) {
        List<List<String>> selection = new ArrayList<>(new ArrayList<>());
        l:
        for (List<String> entry : getRoot()) {
            for (Object object : objects) if (!entry.contains(object.toString())) continue l;
            selection.add(entry);
        }
        return selection;
    }

    @Override
    public List<List<String>> load() {
        try {
            List<List<String>> content = new ArrayList<>();
            if (!getFile().exists()) return content;
            Files.readAllLines(getFile().toPath(), getCharset()).forEach(s -> {
                if (!s.isBlank()) content.add(List.of(s.split(getDelimiter())));
            });
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            createFile();
            try (BufferedWriter writer = Files.newBufferedWriter(getFile().toPath(), getCharset())) {
                for (List<String> entry : getRoot()) writer.write(String.join(getDelimiter(), entry) + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract String getDelimiter();
}

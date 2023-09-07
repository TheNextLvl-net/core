package core.nbt.file;

import core.api.file.FileIO;
import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import core.nbt.tag.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NBTFile extends FileIO<Tag> {

    public NBTFile(File file) {
        super(file);
    }

    public NBTFile(String file) {
        this(new File(file));
    }

    public NBTFile(File parent, String child) {
        this(new File(parent, child));
    }

    public NBTFile(String parent, String child) {
        this(new File(parent, child));
    }

    @Override
    public Tag load() {
        if (!getFile().exists()) return getRoot();
        try (var inputStream = new NBTInputStream(new FileInputStream(getFile()), getCharset())) {
            return inputStream.readTag();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            createFile();
            try (var outputStream = new NBTOutputStream(new FileOutputStream(getFile()), getCharset())) {
                outputStream.writeTag(getRoot());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

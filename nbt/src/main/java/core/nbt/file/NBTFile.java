package core.nbt.file;

import core.api.file.FileIO;
import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import core.nbt.tag.CompoundTag;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NBTFile<R extends CompoundTag, T extends NBTFile<R, T>> extends FileIO<R, T> {
    private String rootName;

    /**
     * Construct a new NBTFile providing a file and default root object
     *
     * @param file the file to read from and write to
     * @param root the default root object
     */
    public NBTFile(File file, R root) {
        super(file, root);
        setRoot(load());
    }

    @Override
    protected R load(File file) {
        if (!exists(file)) return getRoot();
        try (var inputStream = new NBTInputStream(new FileInputStream(file), getCharset())) {
            var entry = inputStream.readNamedTag();
            entry.getValue().ifPresent(this::setRootName);
            return (R) entry.getKey();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T save(File file) {
        try {
            createFile(file);
            try (var outputStream = new NBTOutputStream(new FileOutputStream(file), getCharset())) {
                outputStream.writeTag(getRootName(), getRoot());
            }
            return (T) this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

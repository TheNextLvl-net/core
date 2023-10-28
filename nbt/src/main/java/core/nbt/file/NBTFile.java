package core.nbt.file;

import core.api.file.FileIO;
import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import core.nbt.tag.CompoundTag;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NBTFile<R extends CompoundTag> extends FileIO<R> {
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
    public R load() {
        if (!getFile().exists()) return getRoot();
        try (var inputStream = new NBTInputStream(new FileInputStream(getFile()), getCharset())) {
            return (R) inputStream.readTag();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NBTFile<R> save() {
        try {
            createFile();
            try (var outputStream = new NBTOutputStream(new FileOutputStream(getFile()), getCharset())) {
                outputStream.writeTag(getRoot());
            }
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NBTFile<R> saveIfAbsent() {
        return (NBTFile<R>) super.saveIfAbsent();
    }
}

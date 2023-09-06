package core.nbt.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import core.api.file.format.GsonFile;
import core.nbt.NBTInputStream;
import core.nbt.NBTOutputStream;
import core.nbt.adapter.*;
import core.nbt.tag.*;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class DataFile<R> extends GsonFile<R> {

    private DataFile(File file, @Nullable R root, Type type, Gson gson) { // TODO: 06.09.23 make this somehow public
        super(file, root, type, gson);
    }

    public DataFile(File file, @Nullable R root, Type type) {
        this(file, root, type, new GsonBuilder()
                .registerTypeAdapter(ByteArrayTag.class, new ByteArrayTagAdapter())
                .registerTypeAdapter(ByteTag.class, new ByteTagAdapter())
                .registerTypeAdapter(CompoundTag.class, new CompoundTagAdapter())
                .registerTypeAdapter(DoubleTag.class, new DoubleTagAdapter())
                .registerTypeAdapter(EscapeTag.class, new EscapeTagAdapter())
                .registerTypeAdapter(FloatTag.class, new FloatTagAdapter())
                .registerTypeAdapter(IntArrayTag.class, new IntArrayTagAdapter())
                .registerTypeAdapter(IntTag.class, new IntTagAdapter())
                .registerTypeAdapter(ListTag.class, new ListTagAdapter())
                .registerTypeAdapter(LongArrayTag.class, new LongArrayTagAdapter())
                .registerTypeAdapter(LongTag.class, new LongTagAdapter())
                .registerTypeAdapter(ShortTag.class, new ShortTagAdapter())
                .registerTypeAdapter(StringTag.class, new StringTagAdapter())
                .registerTypeHierarchyAdapter(Tag.class, new TagAdapter())
                .serializeNulls()
                .create() // TODO: 06.09.23 make the registry extensible
        );
    }

    public DataFile(File file, Type type) {
        this(file, null, type);
    }

    public DataFile(File file, @Nullable R root, TypeToken<R> token) {
        this(file, root, token.getType());
    }

    public DataFile(File file, TypeToken<R> token) {
        this(file, null, token);
    }

    public DataFile(File file, R root) {
        this(file, root, root.getClass());
    }

    @Override
    public R load() {
        if (!getFile().exists()) return getRoot();
        try (var inputStream = new NBTInputStream(new FileInputStream(getFile()), getCharset())) {
            return getGson().fromJson(getGson().toJsonTree(inputStream.readTag()), getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            createFile();
            try (var outputStream = new NBTOutputStream(new FileOutputStream(getFile()), getCharset())) {
                outputStream.writeTag(getGson().fromJson(getGson().toJsonTree(getRoot()), Tag.class));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

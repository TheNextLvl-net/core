package core.nbt;

import core.nbt.tag.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public final class NBTInputStream extends DataInputStream {
    /**
     * @param inputStream the input stream
     * @throws IOException thrown if something goes wrong
     */
    public NBTInputStream(InputStream inputStream) throws IOException {
        super(new DataInputStream(new GZIPInputStream(inputStream)));
    }

    /**
     * Read a nbt object from the stream
     *
     * @return the tag that was read
     * @throws IOException thrown if something goes wrong
     */
    public Tag readTag() throws IOException {
        var type = readByte();
        if (type == EscapeTag.ID) return EscapeTag.INSTANCE;
        var bytes = new byte[readShort()];
        readFully(bytes);
        return readTag(type, new String(bytes, StandardCharsets.UTF_8));
    }

    /**
     * Reads a tag from type
     *
     * @param type The type of the tag
     * @param name The name of the tag
     * @return the tag that was read
     * @throws IOException thrown if something goes wrong
     */
    private Tag readTag(int type, String name) throws IOException {
        if (mapper.containsKey(type)) return mapper.get(type).map(this, name);
        throw new IOException("Unknown tag type: " + type);
    }

    /**
     * Mappings between tag type ids and the corresponding mapping function
     */
    private final Map<Integer, MappingFunction> mapper = new HashMap<>() {{
        put(EscapeTag.ID, (inputStream, name) -> EscapeTag.INSTANCE);
        put(ByteTag.ID, (inputStream, name) -> new ByteTag(name, inputStream.readByte()));
        put(ShortTag.ID, (inputStream, name) -> new ShortTag(name, inputStream.readShort()));
        put(IntTag.ID, (inputStream, name) -> new IntTag(name, inputStream.readInt()));
        put(LongTag.ID, (inputStream, name) -> new LongTag(name, inputStream.readLong()));
        put(FloatTag.ID, (inputStream, name) -> new FloatTag(name, inputStream.readFloat()));
        put(DoubleTag.ID, (inputStream, name) -> new DoubleTag(name, inputStream.readDouble()));
        put(ByteArrayTag.ID, (inputStream, name) -> {
            var length = inputStream.readInt();
            var bytes = new byte[length];
            inputStream.readFully(bytes);
            return new ByteArrayTag(name, bytes);
        });
        put(StringTag.ID, (inputStream, name) -> {
            var length = inputStream.readShort();
            var bytes = new byte[length];
            inputStream.readFully(bytes);
            var value = new String(bytes, StandardCharsets.UTF_8);
            return new StringTag(name, value);
        });
        put(ListTag.ID, (inputStream, name) -> {
            var type = inputStream.readByte();
            var length = inputStream.readInt();
            var list = new ArrayList<Tag>();
            for (var i = 0; i < length; i++) {
                var tag = inputStream.readTag(type, null);
                if (!(tag instanceof EscapeTag)) list.add(tag);
                else throw new IOException("EscapeTag not allowed");
            }
            return new ListTag<>(name, type, list);
        });
        put(CompoundTag.ID, (inputStream, name) -> {
            var value = new HashMap<String, Tag>();
            while (true) {
                var tag = inputStream.readTag();
                if (tag instanceof EscapeTag) break;
                value.put(tag.getName(), tag);
            }
            return new CompoundTag(name, value);
        });
        put(IntArrayTag.ID, (inputStream, name) -> {
            var length = inputStream.readInt();
            var array = new int[length];
            for (var i = 0; i < length; i++)
                array[i] = inputStream.readInt();
            return new IntArrayTag(name, array);
        });
        put(LongArrayTag.ID, (inputStream, name) -> {
            var length = inputStream.readInt();
            var array = new long[length];
            for (var i = 0; i < length; i++)
                array[i] = inputStream.readLong();
            return new LongArrayTag(name, array);
        });
    }};

    /**
     * Register a tag mapping
     *
     * @param typeId   the type id of the tag to map
     * @param function the mapping function
     */
    public void registerMapping(int typeId, MappingFunction function) {
        mapper.put(typeId, function);
    }

    /**
     * A functional interface for mapping tags
     */
    @FunctionalInterface
    public interface MappingFunction {
        @NotNull Tag map(@NotNull NBTInputStream inputStream, @Nullable String name) throws IOException;
    }
}

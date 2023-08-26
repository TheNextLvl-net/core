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
        var type = readByte() & 0xFF;
        if (type == EndTag.TYPE.id()) return readTag(type, "");
        var bytes = new byte[readShort() & 0xFFFF];
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
        if (mapper.containsKey(type)) return mapper.get(type).map(name);
        throw new IOException("Unknown tag type: " + type);
    }

    /**
     * Mappings between tag type ids and the corresponding mapping function
     */
    private final Map<Integer, MappingFunction> mapper = new HashMap<>() {{
        put(EndTag.TYPE.id(), name -> EndTag.INSTANCE);
        put(ByteTag.TYPE.id(), name -> new ByteTag(name, readByte()));
        put(ShortTag.TYPE.id(), name -> new ShortTag(name, readShort()));
        put(IntTag.TYPE.id(), name -> new IntTag(name, readInt()));
        put(LongTag.TYPE.id(), name -> new LongTag(name, readLong()));
        put(FloatTag.TYPE.id(), name -> new FloatTag(name, readFloat()));
        put(DoubleTag.TYPE.id(), name -> new DoubleTag(name, readDouble()));
        put(ByteArrayTag.TYPE.id(), name -> {
            var length = readInt();
            var bytes = new byte[length];
            readFully(bytes);
            return new ByteArrayTag(name, bytes);
        });
        put(StringTag.TYPE.id(), name -> {
            var length = readShort();
            var bytes = new byte[length];
            readFully(bytes);
            var value = new String(bytes, StandardCharsets.UTF_8);
            return new StringTag(name, value);
        });
        put(ListTag.TYPE.id(), name -> {
            var type = readByte();
            var length = readInt();
            var list = new ArrayList<Tag>();
            for (var i = 0; i < length; i++) {
                var tag = readTag(type, null);
                if (!(tag instanceof EndTag)) list.add(tag);
                else throw new IOException("EndTag not allowed");
            }
            return new ListTag<>(name, new Tag.Type("unknown", type), list);
        });
        put(CompoundTag.TYPE.id(), name -> {
            var value = new HashMap<String, Tag>();
            while (true) {
                var tag = readTag();
                if (tag instanceof EndTag) break;
                value.put(tag.getName(), tag);
            }
            return new CompoundTag(name, value);
        });
        put(IntArrayTag.TYPE.id(), name -> {
            var length = readInt();
            var array = new int[length];
            for (var i = 0; i < length; i++) array[i] = readInt();
            return new IntArrayTag(name, array);
        });
        put(LongArrayTag.TYPE.id(), name -> {
            var length = readInt();
            var array = new long[length];
            for (var i = 0; i < length; i++) array[i] = readLong();
            return new LongArrayTag(name, array);
        });
    }};

    /**
     * A functional interface for mapping tags
     */
    @FunctionalInterface
    public interface MappingFunction {
        @NotNull Tag map(@Nullable String name) throws IOException;
    }
}

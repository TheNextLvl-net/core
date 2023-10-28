package core.nbt;

import core.nbt.tag.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

@Getter
public final class NBTInputStream extends DataInputStream {
    private final Charset charset;

    /**
     * @param inputStream the input stream
     * @throws IOException thrown if something goes wrong
     */
    public NBTInputStream(InputStream inputStream) throws IOException {
        this(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * @param charset     the charset of the content
     * @param inputStream the input stream
     * @throws IOException thrown if something goes wrong
     */
    public NBTInputStream(InputStream inputStream, Charset charset) throws IOException {
        super(new DataInputStream(new GZIPInputStream(inputStream)));
        this.charset = charset;
    }

    /**
     * Read a nbt object from the stream
     *
     * @return the tag that was read
     * @throws IOException thrown if something goes wrong
     */
    public Tag readTag() throws IOException {
        return readNamedTag().getKey();
    }

    /**
     * Read a named nbt object from the stream
     *
     * @return the tag that was read
     * @throws IOException thrown if something goes wrong
     */
    public Map.Entry<Tag, Optional<String>> readNamedTag() throws IOException {
        var type = readByte();
        if (type == EscapeTag.ID) return Map.entry(EscapeTag.INSTANCE, Optional.empty());
        var bytes = new byte[readShort()];
        readFully(bytes);
        var name = bytes.length == 0 ? null : new String(bytes, getCharset());
        return Map.entry(readTag(type), Optional.ofNullable(name));
    }

    /**
     * Reads a tag from type
     *
     * @param type The type of the tag
     * @param name The name of the tag
     * @return the tag that was read
     * @throws IOException thrown if something goes wrong
     */
    @ApiStatus.Internal
    public Tag readTag(int type) throws IOException {
        var mapping = mapper.get(type);
        if (mapping != null) return mapping.map(this);
        throw new IllegalArgumentException("Unknown tag type: " + type);
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
            var value = new String(bytes, getCharset());
            return new StringTag(name, value);
        });
        put(ListTag.ID, (inputStream, name) -> {
            var type = inputStream.readByte();
            var length = inputStream.readInt();
            var list = new ArrayList<Tag>();
            for (var i = 0; i < length; i++) {
                var tag = inputStream.readTag(type, null);
                if (!(tag instanceof EscapeTag)) list.add(tag);
                else throw new IllegalArgumentException("EscapeTag not allowed");
            }
            return new ListTag<>(name, list, type);
        });
        put(CompoundTag.ID, (inputStream, name) -> {
            var value = new HashMap<String, Tag>();
            while (true) {
                var tag = inputStream.readTag();
                if (tag instanceof EscapeTag) break;
                value.put(Objects.requireNonNull(tag.getName(), "name"), tag);
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
        Tag map(NBTInputStream inputStream) throws IOException;
    }
}

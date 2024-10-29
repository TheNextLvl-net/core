package core.nbt;

import core.nbt.tag.EscapeTag;
import core.nbt.tag.Tag;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

@Getter
public final class NBTOutputStream extends DataOutputStream {
    private final @NonNull Charset charset;

    /**
     * Create a nbt output stream
     *
     * @param outputStream the stream to write to
     * @throws IOException thrown if something goes wrong
     */
    public NBTOutputStream(@NonNull OutputStream outputStream) throws IOException {
        this(outputStream, StandardCharsets.UTF_8);
    }

    /**
     * Create a nbt output stream
     *
     * @param charset      the charset to write the content with
     * @param outputStream the stream to write to
     * @throws IOException thrown if something goes wrong
     */
    public NBTOutputStream(@NonNull OutputStream outputStream, @NonNull Charset charset) throws IOException {
        super(new GZIPOutputStream(outputStream));
        this.charset = charset;
    }

    /**
     * Write a tag to the output stream
     *
     * @param name the name to write
     * @param tag  the tag to write
     * @throws IOException              thrown if something goes wrong
     * @throws IllegalArgumentException thrown if an escape tag was provided
     */
    public void writeTag(@Nullable String name, @NonNull Tag tag) throws IOException, IllegalArgumentException {
        if (tag instanceof EscapeTag) throw new IllegalArgumentException("EscapeTag not allowed");
        var bytes = name != null ? name.getBytes(getCharset()) : new byte[0];
        writeByte(tag.getTypeId());
        writeShort(bytes.length);
        write(bytes);
        tag.write(this);
    }
}

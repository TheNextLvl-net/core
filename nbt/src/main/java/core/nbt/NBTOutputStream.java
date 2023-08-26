package core.nbt;

import core.nbt.tag.EndTag;
import core.nbt.tag.Tag;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

public final class NBTOutputStream extends DataOutputStream {

    public NBTOutputStream(OutputStream outputStream) throws IOException {
        super(new GZIPOutputStream(outputStream));
    }

    public void writeTag(Tag tag) throws IOException {
        if (tag instanceof EndTag) throw new IOException("EndTag not allowed");
        var name = tag.getName() != null ? tag.getName() : "";
        var bytes = name.getBytes(StandardCharsets.UTF_8);
        writeByte(tag.getType().id());
        writeShort(bytes.length);
        write(bytes);
        tag.write(this);
    }
}

package core.nbt.test;

import core.io.IO;
import core.nbt.file.NBTFile;
import core.nbt.tag.CompoundTag;
import core.nbt.tag.DoubleTag;
import core.nbt.tag.FloatTag;
import core.nbt.tag.ListTag;

public class NBTFileTest {
    public static void main(String[] args) {
        var file = new NBTFile<>(IO.of("player.dat"), new CompoundTag());
        var root = file.getRoot();
        root.forEach((s, tag) -> System.out.println(s + ": " + tag));
        var pos = root.getOrDefault("Pos", new ListTag<>(DoubleTag.ID));
        var rotation = root.getOrDefault("Rotation", new ListTag<>(FloatTag.ID));
        pos.set(0, new DoubleTag(0d));
        pos.set(1, new DoubleTag(1d));
        pos.set(2, new DoubleTag(2d));
        rotation.set(0, new FloatTag(0f));
        rotation.set(1, new FloatTag(1f));
        new NBTFile<>(IO.of("test.dat"), root).save();
    }
}

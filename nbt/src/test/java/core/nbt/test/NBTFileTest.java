package core.nbt.test;

import core.io.IO;
import core.nbt.file.NBTFile;
import core.nbt.tag.CompoundTag;
import core.nbt.tag.DoubleTag;
import core.nbt.tag.FloatTag;

public class NBTFileTest {
    public static void main(String[] args) {
        var file = new NBTFile<>(IO.of("player.dat"), new CompoundTag());
        var root = file.getRoot();
        root.forEach((s, tag) -> System.out.println(s + ": " + tag));
        var pos = root.<DoubleTag>getAsList("Pos");
        var rotation = root.<FloatTag>getAsList("Rotation");
        pos.set(0, new DoubleTag(0d));
        pos.set(1, new DoubleTag(1d));
        pos.set(2, new DoubleTag(2d));
        rotation.set(0, new FloatTag(0f));
        rotation.set(1, new FloatTag(1f));
        new NBTFile<>(IO.of("test.dat"), root).save();
    }
}

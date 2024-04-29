package core.nbt.test;

import core.io.IO;
import core.nbt.file.NBTFile;
import core.nbt.tag.CompoundTag;
import core.nbt.tag.DoubleTag;
import core.nbt.tag.FloatTag;
import core.nbt.tag.ListTag;

public class NBTFileTest {
    public static void main(String[] args) {
        var file = new NBTFile<>(IO.of("test.dat"), new CompoundTag());
        var root = file.getRoot();

        root.forEach((s, tag) -> System.out.println(s + ": " + tag));

        var pos = root.getOrAdd("Pos", new ListTag<>(DoubleTag.ID));
        var rotation = root.getOrAdd("Rotation", new ListTag<>(FloatTag.ID));

        pos.clear();
        rotation.clear();

        pos.add(new DoubleTag(2d));
        pos.add(new DoubleTag(5d));
        pos.add(new DoubleTag(9d));

        rotation.add(new FloatTag(-23f));
        rotation.add(new FloatTag(441f));

        file.save();
    }
}

package core.nbt.test;

import core.io.IO;
import core.nbt.file.NBTFile;
import core.nbt.tag.CompoundTag;

public class NBTFileTest {
    public static void main(String[] args) {
        var file = new NBTFile<>(IO.of("lol/nbt/test", "t.dat"), new CompoundTag());
        System.out.println(file.getRoot().get("test"));
        file.getRoot().add("test", "lolll");
        file.setRootName("das ist lustig");
        file.saveIfAbsent();
    }
}

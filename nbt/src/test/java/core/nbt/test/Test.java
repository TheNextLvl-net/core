package core.nbt.test;

import core.nbt.NBTInputStream;

import java.io.FileInputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        try (var inputStream = new NBTInputStream(new FileInputStream("/home/david/Desktop/Coding/paper/1.20.1/worlds/world/level.dat"))) {
            System.out.println(inputStream.readTag());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package core.file.test;

import core.file.format.GsonFile;
import core.io.IO;

import java.util.UUID;

public class GsonFileTest {
    public static void main(String[] args) {
        var file = new GsonFile<>(IO.of("hey", "gson-test.json"), new Test(
                "test", UUID.randomUUID()
        ));
        System.out.println(file.getRoot());
        file.getRoot().name = "lol";
        file.getRoot().uuid = UUID.randomUUID();
        file.save();
    }

    private static class Test {
        private String name;
        private UUID uuid;

        public Test(String name, UUID uuid) {
            this.name = name;
            this.uuid = uuid;
        }

        @Override
        public String toString() {
            return "Test{" +
                   "name='" + name + '\'' +
                   ", uuid=" + uuid +
                   '}';
        }
    }
}

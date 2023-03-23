package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    private static final String drct = "src/main/java/client/data/";

    public static void saveFile(String name, byte[] bytes) throws IOException {
        Files.write(Path.of(drct + name), bytes);
    }

    public static byte[] readFileBytes(String name) throws IOException {
        return Files.readAllBytes(Paths.get(drct + name));
    }

    public static boolean checkFileExists(String name){
        return Files.exists(Path.of(drct + name));
    }
}
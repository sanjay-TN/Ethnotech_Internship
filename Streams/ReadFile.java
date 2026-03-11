package Streams;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.stream.Stream;

public class ReadFile {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\sanja\\Desktop\\Ethnotech\\user.txt";

        
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
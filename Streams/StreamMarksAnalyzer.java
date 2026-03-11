package Streams;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
// import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Collectors;
// import java.util.stream.Stream;

public class StreamMarksAnalyzer {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\sanja\\Desktop\\Ethnotech\\marks.txt";

       try {
            // 1. Read all lines into a List so we can reuse the data
            List<Integer> marks = Files.lines(Paths.get(fileName))
                                       .map(String::trim)
                                       .map(Integer::parseInt)
                                       .collect(Collectors.toList());

            // 2. Separate Total
            int total = marks.stream()
                             .mapToInt(Integer::intValue)
                             .sum();

            // 3. Separate Average (Returns OptionalDouble because list could be empty)
            OptionalDouble average = marks.stream()
                                          .mapToInt(Integer::intValue)
                                          .average();

            // 4. Separate Highest (Returns OptionalInt because list could be empty)
            OptionalInt highest = marks.stream()
                                       .mapToInt(Integer::intValue)
                                       .max();
            
            System.out.println("Total: " + total);
            System.out.println("Average: " + average);
            System.out.println("Highest mark: " + highest);

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
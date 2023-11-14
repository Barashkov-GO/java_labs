import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

public class ReadTextFile {
    public static void main(String[] args) {
        createAndWriteToFile();
        readFromFile();
    }

    private static void createAndWriteToFile() {
        Path filePath = Paths.get("example.txt");
        String[] lines = {
                "Привет, это строка 1.",
                "Это строка 2.",
                "И вот еще одна строка."
        };

        try {
            Files.write(filePath, List.of(lines));
            System.out.println("Файл успешно создан и заполнен.");
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    private static void readFromFile() {
        Path filePath = Paths.get("example.txt");
        try {
            List<String> lines = Files.readAllLines(filePath);
            System.out.println("Содержимое файла:");
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}

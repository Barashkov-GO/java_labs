package FileProcessingSystem.src;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class FileProcessingSystem {
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4};
        ExecutorService generatorExecutor = Executors.newSingleThreadExecutor();
        // Один генератор файлов

        ExecutorService processorExecutor = Executors.newFixedThreadPool(3);
        // 3 обработчика файлов

        BlockingQueue<File> fileQueue = new LinkedBlockingQueue<>(5);
        // Очередь из файлов вместимостью 5 штук

        generatorExecutor.execute(new FileGenerator(fileQueue));
        // Создание генератора

        processorExecutor.execute(new FileProcessor(fileQueue, FileType.JSON));
        processorExecutor.execute(new FileProcessor(fileQueue, FileType.XML));
        processorExecutor.execute(new FileProcessor(fileQueue, FileType.XLS));
        // Создание обработчиков на каждый тип файла

        generatorExecutor.shutdown();
        processorExecutor.shutdown();
    }
}

enum FileType {
    // Тип файла в формате enum
    JSON, XML, XLS
}

class File {
    // Класс файла, хранящий размер и тип файла
    private FileType type;
    private int size;

    public File(FileType type, int size) {
        this.type = type;
        this.size = size;
    }

    public FileType getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
}

class FileGenerator implements Runnable {
    // Класс генератора файлов
    private BlockingQueue<File> fileQueue;

    private Random random = new Random();

    public FileGenerator(BlockingQueue<File> fileQueue) {
        this.fileQueue = fileQueue;
    }

    @Override
    public void run() {
        // Переопределение метода run
        while (true) {
            FileType fileType = FileType.values()[random.nextInt(FileType.values().length)];
            int fileSize = 10 + random.nextInt(91);
            // Размер файла от 10 до 100

            try {
                Thread.sleep(100 + random.nextInt(901));
                // Задержка от 100 до 1000 мс
                fileQueue.put(new File(fileType, fileSize));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

class FileProcessor implements Runnable {
    // Класс обработчика файлов
    private BlockingQueue<File> fileQueue;
    private FileType supportedType;

    public FileProcessor(BlockingQueue<File> fileQueue, FileType supportedType) {
        this.fileQueue = fileQueue;
        this.supportedType = supportedType;
    }

    @Override
    public void run() {
        // Переопределение метода run
        while (true) {
            try {
                File file = fileQueue.take();
                if (file.getType() == supportedType) {
                    int processingTime = file.getSize() * 7;
                    Thread.sleep(processingTime);
                    System.out.println("Обработан файл типа " + supportedType + " размером " + file.getSize() + "МБ за " + processingTime + " мс");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

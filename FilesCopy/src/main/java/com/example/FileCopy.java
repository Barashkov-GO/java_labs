package com.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileCopy {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        FISFOS();

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("FISFOS\n\t - Elapsed Time: " + elapsedTime + " milliseconds");


        startTime = System.currentTimeMillis();

        FileChannel();

        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        System.out.println("FileChannel\n\t - Elapsed Time: " + elapsedTime + " milliseconds");


        startTime = System.currentTimeMillis();

        ApacheCommonsIO();

        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        System.out.println("ApacheCommonsIO\n\t - Elapsed Time: " + elapsedTime + " milliseconds");


        startTime = System.currentTimeMillis();

        FilesClass();

        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        System.out.println("FilesClass\n\t - Elapsed Time: " + elapsedTime + " milliseconds");
    }
    public static void FISFOS() {
        String sourcePath = "sourceFile.txt";
        String destinationPath = "destinationFile.txt";

        try (FileInputStream fis = new FileInputStream(sourcePath);
             FileOutputStream fos = new FileOutputStream(destinationPath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            System.out.println("Файл успешно скопирован (FileInputStream/FileOutputStream).");
        } catch (IOException e) {
            System.err.println("Ошибка при копировании файла: " + e.getMessage());
        }
    }

    public static void FileChannel() {
        String sourcePath = "sourceFile.txt";
        String destinationPath = "destinationFile1.txt";

        try (FileInputStream fis = new FileInputStream(sourcePath);
             FileOutputStream fos = new FileOutputStream(destinationPath);
             FileChannel sourceChannel = fis.getChannel();
             FileChannel destinationChannel = fos.getChannel()) {

            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

            System.out.println("Файл успешно скопирован (FileChannel).");
        } catch (IOException e) {
            System.err.println("Ошибка при копировании файла: " + e.getMessage());
        }
    }

    public static void ApacheCommonsIO() {
        String sourcePath = "sourceFile.txt";
        String destinationPath = "destinationFile2.txt";

        try {
            File sourceFile = new File(sourcePath);
            File destinationFile = new File(destinationPath);

            FileUtils.copyFile(sourceFile, destinationFile);

            System.out.println("Файл успешно скопирован (Apache Commons IO).");
        } catch (IOException e) {
            System.err.println("Ошибка при копировании файла: " + e.getMessage());
        }
    }

    public static void FilesClass() {
        String sourcePath = "sourceFile.txt";
        String destinationPath = "destinationFile3.txt";

        try {
            Path source = Paths.get(sourcePath);
            Path destination = Paths.get(destinationPath);

            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Файл успешно скопирован (Files class).");
        } catch (IOException e) {
            System.err.println("Ошибка при копировании файла: " + e.getMessage());
        }
    }

}
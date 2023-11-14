package com.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.FileInputStream;

public class DirectoryWatcher {

    private static final Map<Path, List<String>> fileStates = new HashMap<>();

    public static void main(String[] args) {
        String directoryPath = "/home/fanat/";

        try {
            watchDirectory(directoryPath);
        } catch (IOException e) {
            System.err.println("Error watching directory: " + e.getMessage());
        }
    }

    private static void watchDirectory(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

        while (true) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                System.err.println("Error taking watch key: " + e.getMessage());
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                Path filePath = (Path) event.context();

                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.println("File created: " + filePath.getFileName());
                } else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("File modified: " + filePath.getFileName());

                    printFileChanges(path.resolve(filePath));
                } else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println("File deleted: " + filePath.getFileName());
                    printFileSizeAndChecksum(path.resolve(filePath));
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

    private static void printFileChanges(Path filePath) {
        try {
            List<String> currentLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<String> previousLines = fileStates.get(filePath);

            System.out.println("Changes in file content:");

            if (previousLines != null) {
                int minSize = Math.min(previousLines.size(), currentLines.size());

                for (int i = 0; i < minSize; i++) {
                    String previousLine = previousLines.get(i);
                    String currentLine = currentLines.get(i);

                    if (!previousLine.equals(currentLine)) {
                        System.out.println("Modified line " + (i + 1) + ":");
                        System.out.println("  Before: " + previousLine);
                        System.out.println("  After : " + currentLine);
                    }
                }
            }

            for (int i = previousLines != null ? previousLines.size() : 0; i < currentLines.size(); i++) {
                System.out.println("Added line " + (i + 1) + ": " + currentLines.get(i));
            }

            fileStates.put(filePath, currentLines);

        } catch (IOException e) {
            System.err.println("Error printing file changes: " + e.getMessage());
        }
    }

    private static void printFileSizeAndChecksum(Path filePath) {
        try {
            long fileSize = Files.size(filePath);
            short checksum = calculateChecksum(filePath.toString());

            System.out.println("File size: " + fileSize + " bytes");
            System.out.println("Checksum: " + checksum);
        } catch (IOException e) {
            System.err.println("Error calculating file size and checksum: " + e.getMessage());
        }
    }

    private static short calculateChecksum(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             FileChannel channel = fis.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(2);

            short checksum = 0;

            while (channel.read(buffer) != -1) {
                buffer.flip();
                checksum ^= buffer.getShort();
                buffer.clear();
            }

            return checksum;
        }
    }
}

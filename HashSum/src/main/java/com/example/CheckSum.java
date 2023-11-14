package com.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CheckSum {

    public static void main(String[] args) {
        String filePath = "../FilesCopy/sourceFile.txt";

        try {
            short checksum = calculateChecksum(filePath);
            System.out.println("Checksum: " + checksum);
        } catch (IOException e) {
            System.err.println("Error calculating checksum: " + e.getMessage());
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

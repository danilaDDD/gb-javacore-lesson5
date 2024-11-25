package ru.gbdanila.corelesson5;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
       createBackup("testdir");
       writeNumbersToFile(new byte[]{1, 2, 3, 1, 2, 1}, "numbers.txt");
       byte[] bytes = readNumbersFromFile("numbers.txt");
        System.out.println(Arrays.toString(bytes));

    }

    private static byte[] readNumbersFromFile(String file) {
        int zipNumber;
        try(DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))){
            zipNumber = inputStream.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        char[] chars = String.valueOf(zipNumber).toCharArray();
        byte[] bytes = new byte[chars.length];

        for (int i = 0; i < chars.length; i++) {
            bytes[i] = Byte.parseByte(String.valueOf(chars[i]));
        }

        return bytes;
    }


    private static void createBackup(String sourceDirectory) throws IOException {
       File sourceFileObj = new File(sourceDirectory);
       if(sourceFileObj.isFile())
           throw new IllegalArgumentException(String.format("source path %s may be directory path file path given", sourceDirectory));

        Path backupDirectory = Path.of("backup");
        if(backupDirectory.toFile().exists()){
            for (File file : backupDirectory.toFile().listFiles()) {
                Files.delete(file.toPath());
            }
            Files.delete(backupDirectory);
        }
        Files.createDirectory(backupDirectory);

        for(File childFile: sourceFileObj.listFiles()){
            Path copiedFilePath = Paths.get(new File(backupDirectory.toFile(), childFile.getName()).getAbsolutePath());
            Path childFilePath = Paths.get(childFile.getAbsolutePath());
            Files.copy(childFilePath, copiedFilePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void writeNumbersToFile(byte[] bytes, String filePath) throws IOException {
        if(bytes.length > 9)
            throw new IllegalArgumentException(String.format("bytes array may be length <= 9 %s given", bytes.length));

        int zipNumbers = zipNumbers(bytes);
        try(DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(filePath))){
           dataOutputStream.writeInt(zipNumbers);
        }
    }

    private static int zipNumbers(byte[] bytes) {
        int pow = 1;
        for (int i = 0; i < bytes.length - 1; i++) {
            pow *= 10;
        }

        int target = 0;
        for (byte aByte : bytes) {
            byte digit = (byte) (aByte % 10);
            target += digit * pow;
            pow /= 10;
        }

        return target;
    }
}
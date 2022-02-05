package org.joelson.mattias.turfgame.apiv4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class FeedsPartitioner {

    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.printf("Usage:%n\t%s [version] [last date]", FeedsPartitioner.class.getName());
            System.exit(-1);
        }
        String version = args[0];
        String date = args[1];

        Path partitionDirectory = Path.of(".", "partition");
        if (Files.exists(partitionDirectory)) {
            System.out.printf("Can not create directory %s - file exists.", partitionDirectory);
            System.exit(-1);
        }

        Files.createDirectory(partitionDirectory);
        System.out.printf("<create directory %s>%n", partitionDirectory);

//        int noFiles = Files.list(Path.of(".")).mapToInt(path -> 1).sum();
//        System.out.println("noFiles: " + noFiles);
//
//        noFiles = Files.list(Path.of("."))
//                .filter(path -> pathNotLarger(date, path))
//                .mapToInt(path -> 1).sum();
//        System.out.println("noFiles: " + noFiles);
        Files.list(Path.of("."))
                .filter(path -> includeFile(date, path))
                .forEach(path -> {
                    moveFile(partitionDirectory, path);
                });

        String firstDate = Files.list(partitionDirectory)
                .filter(path -> includeFile(date, path))
                .map(path -> getDate(path))
                .sorted()
                .findFirst().orElseThrow();
        System.out.println("firstDate: " + firstDate);

        Path finalPartition = Path.of(".", "feeds_" + version + '_' + firstDate + ".win");
        System.out.printf("<move %s to %s>%n", partitionDirectory, finalPartition);
        Files.move(partitionDirectory, finalPartition);
        System.out.printf("archive:%n\t7z a %s %s%n", finalPartition.getFileName() + ".zip", finalPartition);
    }

    private static void moveFile(Path partitionDirectory, Path path) {
        Path destination = partitionDirectory.resolve(path.getFileName());
        System.out.printf("<move %s to %s>%n", path, destination);
        try {
            Files.move(path, destination);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static boolean includeFile(String date, Path path) {
        try {
            return !Files.isDirectory(path) && getDate(path).compareTo(date) <= 0;
        } catch (RuntimeException e) {
            return false;
        }
//        String filename = path.getFileName().toString();
//        int startIndex = -1;
//        for (int i = 0; i < filename.length(); i += 1) {
//            if (Character.isDigit(filename.charAt(i))) {
//                startIndex = i;
//                break;
//            }
//        }
//        if (startIndex < 0) {
//            return false;
//        }
//        return filename.substring(startIndex, startIndex + date.length()).compareTo(date) <= 0;
    }

    private static String getDate(Path path) {
        String filename = path.getFileName().toString();
        int startIndex = -1;
        for (int i = 0; i < filename.length(); i += 1) {
            if (Character.isDigit(filename.charAt(i))) {
                startIndex = i;
                break;
            }
        }
        if (startIndex < 0) {
            throw new IndexOutOfBoundsException("Could not find date!");
        }
        if (Character.isDigit(filename.charAt(startIndex + 1))
                && Character.isDigit(filename.charAt(startIndex + 2))
                && Character.isDigit(filename.charAt(startIndex + 3))
                && filename.charAt(startIndex + 4) == '-'
                && Character.isDigit(filename.charAt(startIndex + 5))
                && Character.isDigit(filename.charAt(startIndex + 6))
                && filename.charAt(startIndex + 7) == '-'
                && Character.isDigit(filename.charAt(startIndex + 8))
                && Character.isDigit(filename.charAt(startIndex + 9))) {
            return filename.substring(startIndex, startIndex + 10);
        }
        throw new NumberFormatException("Bad date format: " + filename.substring(startIndex, startIndex + 10));
    }
}


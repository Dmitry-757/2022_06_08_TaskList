package org.dng;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static final String fileName = "Tasks.dat";
    private static final Scanner sc = new Scanner(System.in);

    public static Scanner getSc() {
        return sc;
    }

    public static String getFileName() {
        return fileName;
    }


    public static void main(String[] args) {

        Path path1 = Path.of(fileName);
        if (Files.exists(path1)) {
            try {
                Files.delete(path1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("let`s make any tasks:");
        Task task1 = new Task("task1 header", "task1 description", PriorityLevel.MEDIUM,
                LocalDate.now(), LocalDate.of(2022, 06, 12), "Pupkin");
        System.out.println(task1);
        Task task2 = new Task("task2 header", "task2 description", PriorityLevel.MEDIUM,
                LocalDate.now(), LocalDate.of(2022, 06, 15), "Petrov");
        System.out.println(task2);
        Task task3 = new Task("task3 header", "task3 description", PriorityLevel.MEDIUM,
                LocalDate.now(), LocalDate.of(2022, 06, 20), "Sidorov");
        System.out.println(task3);

        System.out.println("**********************************");
        System.out.println("try to read Task objects from file:");
        IOService.readFromFile()
                .forEach(System.out::println);
        System.out.println("**********************************");
        System.out.println("Try to find task with header `task2 header`");
        System.out.println(IOService.readFromFile("task2 header"));

        System.out.println("Try to find task with header `not exist task header`");
        System.out.println(IOService.readFromFile("not exist task header"));

        System.out.println("*******************************************************");
        System.out.println("try to delete task2");
        IOService.deleteTaskFromFile(task2);
        IOService.readFromFile()
                .forEach(System.out::println);

        System.out.println("*******************************************************");
        System.out.println("try to delete task2");
        IOService.deleteTaskFromFile(task2);
        IOService.readFromFile()
                .forEach(System.out::println);

    }


}

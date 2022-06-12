package org.dng;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static final String fileName = "TaskDat.txt";
    private static final Scanner sc = new Scanner(System.in);

    public static Scanner getSc() {
        return sc;
    }

    public static String getFileName() {
        return fileName;
    }


    public static void main(String[] args) {

        Path path1 = Path.of(fileName);
        if(Files.exists(path1)) {
            try {
                Files.delete(path1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Task task1 = new Task("task1 header", "task1 descryption", PriorityLevel.MEDIUM,
                LocalDate.now(), LocalDate.of(2022,06,12), "Pupkin");
        System.out.println(task1);
        Task task2 = new Task("task2 header", "task2 descryption", PriorityLevel.MEDIUM,
                LocalDate.now(), LocalDate.of(2022,06,15), "Petrov");
        System.out.println(task2);

        System.out.println("**********************************");
        System.out.println("try read Task objects from file:");
        IOService.readFromFile()
                .forEach(System.out::println);
    }



}

package org.dng;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

//Main idea is: to convert object by ObjectOutputStream to byte array,
// after that write byte array to FileOutputStream
//put the some special symbols at after byte array with object - for example "&&&" - it means "end of object"

public class IOService {

    public static void writeToFile(Task task) {
        //task->oos->toByteArray()->fos
        try (FileOutputStream fos = new FileOutputStream(Main.getFileName(), true);
             ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(task);//task -> objOutputStream -> FileOutputStream

            fos.write("&&&".getBytes());
            fos.flush();//clear buffer

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static List<Task> readFromFile() {
        List<Task> taskList = new LinkedList<>();

        try (
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(Main.getFileName()))
        ) {
            //queue contains symbols "end of object"
            Queue<Integer> myQEndOfObj = new LinkedList<>();
            myQEndOfObj.add(38);//(char)38 = "&"
            myQEndOfObj.add(38);
            myQEndOfObj.add(38);

            Queue<Integer> myQ = new LinkedList<>();//queue for last 3 symbols
            Deque<Integer> myQObj = new LinkedList<>();
            int i;
            while ((i = bis.read()) != -1) {
                //System.out.print((char)i);
                myQ.add(i);
                if (myQ.size() > 3) { //we need for only three last symbols
                    myQ.poll();
                }
                myQObj.add(i);

                if (myQ.equals(myQEndOfObj)) {
                    //System.out.println("\nfind end of obj!!!");
                    //we find end of object and now need to remove 3 symbols of "end of obj"
                    // from queue that consists object"
                    myQObj.pollLast();
                    myQObj.pollLast();
                    myQObj.pollLast();

                    //тут надо перевести myQObj в объект и зачистить myQObj
                    Task task = byteToObj(myQObj);
                    if (task != null)
                        taskList.add(byteToObj(myQObj));
                    myQObj.clear();
                }
            }
        } catch (IOException e) {
            System.out.println("IOException by bis.read()" + e.getMessage());
        }

        return taskList;
    }


    public static Task readFromFile(String header) {
        Task task = null;
        try (
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(Main.getFileName()))
        ) {
            //queue contains symbols "end of object"
            Queue<Integer> myQEndOfObj = new LinkedList<>();
            myQEndOfObj.add(38);//(char)38 = "&"
            myQEndOfObj.add(38);
            myQEndOfObj.add(38);

            Queue<Integer> myQ = new LinkedList<>();//queue for last 3 symbols
            Deque<Integer> myQObj = new LinkedList<>();
            int i;
            while ((i = bis.read()) != -1) {
                //System.out.print((char)i);
                myQ.add(i);
                if (myQ.size() > 3) { //we need for only three last symbols
                    myQ.poll();
                }
                myQObj.add(i);

                if (myQ.equals(myQEndOfObj)) {
                    //System.out.println("\nfind end of obj!!!");
                    //we find end of object and now need to remove 3 symbols of "end of obj"
                    // from queue that consists object"
                    myQObj.pollLast();
                    myQObj.pollLast();
                    myQObj.pollLast();

                    //тут надо перевести myQObj в объект и зачистить myQObj
                    Task tsk = byteToObj(myQObj);
                    if (tsk != null)
                        if (tsk.getHeader().equals(header))
                            return tsk;
                    myQObj.clear();
                }
            }
        } catch (IOException e) {
            System.out.println("IOException by bis.read()" + e.getMessage());
        }
        return task;
    }


    public static int[] readFromFile(Task item) {

        int[] result = new int[2];//result[0] - start array, result[1] - length of  array
        try (
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(Main.getFileName()))
        ) {
            //queue contains symbols "end of object"
            Queue<Integer> myQEndOfObj = new LinkedList<>();
            myQEndOfObj.add(38);//(char)38 = "&"
            myQEndOfObj.add(38);
            myQEndOfObj.add(38);

            Queue<Integer> myQ = new LinkedList<>();//queue for last 3 symbols
            Deque<Integer> myQObj = new LinkedList<>();
            int i;
            int currentPos = 0;
            while ((i = bis.read()) != -1) {
                currentPos++;
                //System.out.print((char)i);
                myQ.add(i);
                if (myQ.size() > 3) { //we need for only three last symbols
                    myQ.poll();
                }
                myQObj.add(i);
                if (myQ.equals(myQEndOfObj)) {
                    //System.out.println("\nfind end of obj!!!");

                    //we find end of object and now need to remove 3 symbols of "end of obj"
                    // from queue that consists object"
                    myQObj.pollLast();
                    myQObj.pollLast();
                    myQObj.pollLast();

                    //тут надо перевести myQObj в объект и зачистить myQObj
                    Task tsk = byteToObj(myQObj);
                    if (tsk != null)
                        if (tsk.equals(item)) {
                            result[1] = myQObj.size() + 3;//записаны байты самого объекта + 3 байта символа "конца объекта"
                            result[0] = currentPos - result[1] + 1;

                            return result;
                        }
                    myQObj.clear();
                }
            }
        } catch (IOException e) {
            System.out.println("IOException by bis.read()" + e.getMessage());
        }
        return result;
    }


    public static Task byteToObj(Deque<Integer> myQObj) {
        byte[] bytes = new byte[myQObj.size()];
        int j = 0;
        for (Integer I : myQObj) {
            bytes[j++] = I.byteValue();
        }

        Task task = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
//                // add filter before readObject
//                ois.setObjectInputFilter(filter);
            task = (Task) ois.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException by readObject " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException by ByteArrayInputStream or ObjectInputStream" + e.getMessage());
            e.printStackTrace();
        }
        return task;
    }

    public static void deleteTaskFromFile(Task task) {
        if (task ==null)
            return;
        int[] buf = readFromFile(task);
        int start = buf[0];
        int length = buf[1];
        try (
                //BufferedInputStream bis = new BufferedInputStream(new FileInputStream(Main.getFileName()));
                FileInputStream fis = new FileInputStream(Main.getFileName());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("temp.dat", false))
        ) {
            int i, currentPos = 1;
            while ((i = fis.read()) != -1) {
                bos.write(i);

                //skip read bytes of object which must be deleted
                if (currentPos == start)
                    fis.skip(length);

                currentPos++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        }
        Path source = Path.of("temp.dat");
        Path target = Path.of(Main.getFileName());
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("IOException during coping " + e.getMessage());
        }

    }
}

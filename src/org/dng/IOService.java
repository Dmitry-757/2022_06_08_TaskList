package org.dng;

import java.io.*;
import java.util.*;

//Main idea is: to convert object to byte array, after that write byte array to BufferedOutputStream
// and then write it to FileOutputStream
//put the three last symbols at the end of each byte array - "&&&" - it means "end of object"

public class IOService {
    public static void writeToFile(Task task) {
        //task->oos->boas->toByteArray()->bos->fos
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos);

             BufferedOutputStream bos = new BufferedOutputStream(
                     new FileOutputStream(Main.getFileName(), true)
             );
        ) {
            oos.writeObject(task);//task -> objOutputStream -> ByteArrayOutputStream(baos)
            bos.write(baos.toByteArray()); //ByteArrayOutputStrem(baos) -> BufferedOutputStream -> FileOutPutStream

            bos.write("&&&".getBytes());
//            System.out.println("end of object = "+"&&&".getBytes());
            bos.flush();//clear buffer

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    public static List<Task> readFromFile() {
        List<Task> taskList = new LinkedList<>();

        try (
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(Main.getFileName()));
        ) {
            //queue consist symbols "end of object"
            Queue<Integer> myQEndOfObj = new LinkedList<>();
            myQEndOfObj.add(38);//(char)38 = "&"
            myQEndOfObj.add(38);
            myQEndOfObj.add(38);

            Queue<Integer> myQ = new LinkedList<>();//queue for last 3 symbols
            Deque<Integer> myQObj = new LinkedList<>();
            int i = 0;
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
            //System.out.println(task.toString());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException by readObject " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException by ByteArrayInputStream or ObjectInputStream" + e.getMessage());
            e.printStackTrace();
        }
        return task;
    }

}
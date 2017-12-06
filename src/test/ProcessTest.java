package test;

import java.util.Scanner;

public class ProcessTest {

    public static void main(String[] args) {

        try {
            ProcessBuilder pb = new
                    ProcessBuilder("/bin/sh", "-c",
                    "echo 'scale=4;22/7' | bc");
            final Process p=pb.start();
            Scanner scanner = new Scanner(p.getInputStream());
            while(scanner.hasNextLine()){
                System.out.println(scanner.nextLine());
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}


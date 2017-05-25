package servertest;

import utilities.ElapsedTimer;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Simon Lucas on 25/05/2017.
 */
public class SimpleClient {
    public static void main(String[] args) throws Exception {
        int nReps = 10;
        String hostname = "localhost";
        int delay = 1000; // millis

        for (int i=0; i<nReps; i++) {
            Socket socket = new Socket(hostname, SimpleServer.defaultPort);
            PrintStream out = new PrintStream(socket.getOutputStream());
            Scanner scanner = new Scanner(socket.getInputStream());
            ElapsedTimer t = new ElapsedTimer();
            out.println("Hello world");
            String line = scanner.nextLine();
            System.out.println(i + "\t " + line);
            System.out.println("Round trip delay: " + t);
            socket.close();
            Thread.sleep(delay);
        }
    }

}

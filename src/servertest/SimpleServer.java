package servertest;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by sml on 25/05/2017.
 */
public class SimpleServer extends Thread {

    public static int defaultPort = 3000;


    Socket socket;

    public SimpleServer(Socket socket) {
        this.socket = socket;
        start();
    }

    public void run() {

        System.out.println("Started a new Socket: " + socket);

        // decide how much work to do each time
        // for now just read and write a single message
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintStream out = new PrintStream(socket.getOutputStream());
            String received = in.nextLine();
            out.format("Sending back: " + received);

            // may want to close this client side instead
            socket.close();
            System.out.println("Closed socket");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {


        ServerSocket serverSocket = new ServerSocket(defaultPort);
        while (true) {
            Socket socket = serverSocket.accept();
            new SimpleServer(socket);
        }

    }
}

package by.lab3.client;

import by.lab3.Scanner.ScannerUtil;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private PrintWriter writer;
    private boolean running = true;

    public void start() {
        ScannerUtil data = new ScannerUtil();
        System.out.println("Input port");
        int port = data.inputPort();
        System.out.println("Input ip");
        String ip = data.inputString();
        try {
            Socket socket = new Socket(InetAddress.getByName(ip), port);
            ClientInput input = new ClientInput(this);
            input.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String request;
            while ((request = reader.readLine()) != null) {
                System.out.println(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public boolean isRunning() {
        return running;
    }

}

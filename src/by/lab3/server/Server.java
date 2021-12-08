package by.lab3.server;

import by.lab3.Scanner.ScannerUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

    private static final int BACKLOG = 40;

    private ServerSocket serverSocket;

    @Override
    public void run() {
        System.out.println("Input port");
        ScannerUtil sc = new ScannerUtil();
        int port = sc.inputPort();
        try {
            serverSocket = new ServerSocket(port, BACKLOG, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server is running");

        startConnectionObserve();
    }

    private void startConnectionObserve() {
        while (true) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket, this);
                connectionHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect(ConnectionHandler connectionHandler) {
        try {
            connectionHandler.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client disconnected");
    }
}

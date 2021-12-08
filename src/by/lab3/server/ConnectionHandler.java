package by.lab3.server;

import by.lab3.server.command.CommandProvider;
import by.lab3.server.command.impl.DisconnectCommand;
import by.lab3.server.command.ICommand;
import by.lab3.server.command.exception.CommandException;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ConnectionHandler extends Thread {
    private static final String WELCOME_MESSAGE = """
            Available commands:
            * AUTH USER/MANAGER
            * DISCONNECT
            * VIEW
            * CREATE (firstname) (lastname)
            * EDIT (id) (firstname) (lastname)""";

    private final Socket socket;
    private final Server server;

    private BufferedReader reader;
    private PrintWriter writer;

    public ConnectionHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendMessage(WELCOME_MESSAGE);
        observeCommands();

        server.disconnect(this);
    }

    private void observeCommands() {
        var isRunning = true;
        do {
            String request = readMessage();
            if (request == null) {
                break;
            }
            isRunning = processRequest(request);
        } while (isRunning);
    }

    private boolean processRequest(String request) {
        try {
            ICommand command = CommandProvider.getInstance().getCommand(request);
            String response = command.execute(this, request);
            sendMessage(response);

            if (command instanceof DisconnectCommand) {
                return false;
            }
        } catch (CommandException e) {
            e.printStackTrace();
            sendMessage(e.getMessage());
        }

        return true;
    }

    private String readMessage() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionHandler that = (ConnectionHandler) o;
        return socket.equals(that.socket) && server.equals(that.server);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, server);
    }
}

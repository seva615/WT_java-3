package by.lab3.server.command;

import by.lab3.server.command.exception.CommandException;
import by.lab3.server.command.impl.*;

public class CommandProvider {
    private static final CommandProvider INSTANCE = new CommandProvider();

    private CommandProvider() {
    }

    public static CommandProvider getInstance() {
        return INSTANCE;
    }

    public ICommand getCommand(String request) throws CommandException {
        if (request == null)
            throw new CommandException("No command in request");

        String[] arguments = request.split(" ");

        if (arguments.length < 1)
            throw new CommandException("No command in request");

        return switch (arguments[0]) {
            case "AUTH" -> new AuthenticationCommand();
            case "DISCONNECT" -> new DisconnectCommand();
            case "EDIT" -> new EditCommand();
            case "VIEW" -> new ViewCommand();
            case "CREATE" -> new CreateCommand();
            default -> throw new CommandException("No such command");
        };
    }
}

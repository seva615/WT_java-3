package by.lab3.server.command.impl;

import by.lab3.server.command.exception.CommandException;
import by.lab3.server.command.ICommand;
import by.lab3.server.command.exception.CommandException;
import by.lab3.server.model.AuthenticationType;
import by.lab3.server.service.ServiceFactory;

public class CreateCommand implements ICommand {
    public String execute(Object caller, String request) throws CommandException {
        String[] arguments = request.split(" ");
        if (arguments.length != 3) throw new CommandException("CREATE invalid syntax");

        if (ServiceFactory.getInstance().getAuthService().getAuthType(caller) != AuthenticationType.ADMINISTRATOR)
            return "Should be Administrator";

        ServiceFactory.getInstance().getCaseService().addUser(arguments[1], arguments[2]);
        return "Success";
    }
}

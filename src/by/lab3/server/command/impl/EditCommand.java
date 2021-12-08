package by.lab3.server.command.impl;

import by.lab3.server.command.ICommand;
import by.lab3.server.command.exception.CommandException;
import by.lab3.server.model.AuthenticationType;
import by.lab3.server.service.ServiceFactory;

public class EditCommand implements ICommand {
    public String execute(Object caller, String request) throws CommandException {
        String[] arguments = request.split(" ");
        if (arguments.length != 4) throw new CommandException("Invalid syntax EDIT");

        if (ServiceFactory.getInstance().getAuthService().getAuthType(caller) != AuthenticationType.ADMINISTRATOR)
            return "Should be Administrator";

        int id;
        try {
            id = Integer.parseInt(arguments[1]);
        } catch (NumberFormatException ignored) {
            return "Invalid id";
        }

        if (!ServiceFactory.getInstance().getCaseService().containsUser(id))
            return "No such case";

        ServiceFactory.getInstance().getCaseService().editUser(id, arguments[2], arguments[3]);
        return "Success";
    }
}

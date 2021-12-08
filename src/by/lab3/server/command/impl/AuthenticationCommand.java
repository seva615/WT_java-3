package by.lab3.server.command.impl;

import by.lab3.server.command.ICommand;
import by.lab3.server.command.exception.CommandException;
import by.lab3.server.model.AuthenticationType;
import by.lab3.server.service.ServiceFactory;

public class AuthenticationCommand implements ICommand {
    @Override
    public String execute(Object caller, String request) throws CommandException {
        String[] arguments = request.split(" ");
        if (arguments.length != 2) throw new CommandException("AUTH command should contain 1 argument");
        AuthenticationType authenticationType;
        try {
            authenticationType = AuthenticationType.valueOf(arguments[1]);
        } catch (IllegalArgumentException e) {
            throw new CommandException("No such auth type");
        }

        ServiceFactory.getInstance().getAuthService().setAuthType(caller, authenticationType);
        return "Success.";
    }
}

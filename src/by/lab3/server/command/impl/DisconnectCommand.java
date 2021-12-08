package by.lab3.server.command.impl;

import by.lab3.server.command.ICommand;
import by.lab3.server.command.exception.CommandException;
import by.lab3.server.model.AuthenticationType;
import by.lab3.server.service.ServiceFactory;

public class DisconnectCommand implements ICommand {
    public String execute(Object caller, String request) throws CommandException {
        ServiceFactory.getInstance().getAuthService().setAuthType(caller, AuthenticationType.GUEST);
        return "Disconnected!";
    }
}

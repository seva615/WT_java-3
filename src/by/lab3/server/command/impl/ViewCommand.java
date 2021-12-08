package by.lab3.server.command.impl;

import by.lab3.server.command.ICommand;
import by.lab3.server.command.exception.CommandException;
import by.lab3.server.model.AuthenticationType;
import by.lab3.server.model.User;
import by.lab3.server.service.ServiceFactory;

import java.util.List;

public class ViewCommand implements ICommand {
    public String execute(Object caller, String request) throws CommandException {
        if (ServiceFactory.getInstance().getAuthService().getAuthType(caller) == AuthenticationType.ADMINISTRATOR)
            return "Should be authenticated";

        List<User> people = ServiceFactory.getInstance().getCaseService().getAll();
        return toOutput(people);
    }

    private static String toOutput(List<User> people) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("[\n");
        for (var box : people) {
            resultBuilder.append("\t").append(box.toString()).append("\n");
        }
        resultBuilder.append("]");
        return resultBuilder.toString();
    }
}

package by.lab3.server.command;

import by.lab3.server.command.exception.CommandException;

public interface ICommand {
    String execute(Object caller, String request) throws CommandException;
}

package client.tools;

import client.Client;
import clientAndServer.commands.Command;
import clientAndServer.commands.commandsClasses.withArgs.MpaaFilterCommand;
import clientAndServer.commands.commandsClasses.withArgs.RemoveByIdCommand;
import clientAndServer.commands.commandsClasses.withArgsElems.InsertAtCommand;
import clientAndServer.commands.commandsClasses.withArgsElems.UpdateCommand;
import clientAndServer.commands.commandsClasses.withElements.AddCommand;
import clientAndServer.commands.commandsClasses.withElements.RemoveGreaterCommand;
import clientAndServer.commands.commandsClasses.withoutAll.*;
import clientAndServer.exeptions.ExecCommException;
import clientAndServer.exeptions.InvalidNameException;
import clientAndServer.exeptions.NonArgsExeption;
import clientAndServer.exeptions.TooManyArgsException;
import clientAndServer.tools.consoleTools.ConsoleReader;

import java.io.IOException;
import java.net.DatagramSocket;

public class CommandBuilder {

    public Command build(String arraysOfParams, String params, DatagramSocket clientSocket, String username, String password) throws TooManyArgsException, NonArgsExeption, InvalidNameException, ExecCommException, IOException {
        Command command = null;
        switch (arraysOfParams) {
            case("log_in"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new LoginCommand(arraysOfParams, params, username, password);
                break;
            case("register"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new RegisterCommand(arraysOfParams, params, username, password);
                break;
            case ("sign_out"):
                Client.setUsername("");
                Client.setPassword("");
                Authorizator.setAutFlag(false);
                Client.setNeedToAuthorize(true);
                break;
            case ("show"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new ShowCommand(arraysOfParams, params, username, password);
                break;
            case ("clear"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new ClearCommand(arraysOfParams, params, username, password);
                break;
            case ("group_counting_by_id"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new GroupCountingIdCommand(arraysOfParams, params, username, password);
                break;
            case ("help"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new HelpCommand(arraysOfParams, params, username, password);
                break;
            case ("info"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new InfoCommand(arraysOfParams, params, username, password);
                break;
            case ("print_descending"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new PrintDescendingCommand(arraysOfParams, params, username, password);
                break;
            case ("history"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new HistoryCommand(arraysOfParams, params, username, password);
                break;
            case ("exit"):
                if (!params.equals("")){throw new TooManyArgsException();}
                ConsoleReader.setToExit(true);
                break;
            case ("execute_script"):
                if (params.equals("")){throw new NonArgsExeption();}
                if (!FilesSafe.getFilesList().contains(params)) {
                    Scripter scripter = new Scripter();
                    scripter.read(params);
                    scripter.executeScript(clientSocket);
                    break;
                } else {
                    System.out.println("Recursion found");
                    FilesSafe.clear();
                }
                break;
            case ("filter_less_than_mpaa_rating"):
                if (params.equals("")){throw new NonArgsExeption();}
                MpaaFilterCommand.tryParse(params);
                command = new MpaaFilterCommand(arraysOfParams, params, username, password);
                break;
            case ("remove_by_id"):
                if (params.equals("")){throw new NonArgsExeption();}
                RemoveByIdCommand.tryParse(params);
                command = new RemoveByIdCommand(arraysOfParams, params, username, password);
                break;
            case ("add"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new AddCommand(arraysOfParams, params, username);
                break;
            case ("remove_greater"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new RemoveGreaterCommand(arraysOfParams, params, username);
                break;
            case ("insert_at"):
                if (params.equals("")){throw new NonArgsExeption();}
                InsertAtCommand.tryParse(params);
                command = new InsertAtCommand(arraysOfParams, params, username);
                break;
            case ("update"):
                if (params.equals("")){throw new NonArgsExeption();}
                UpdateCommand.tryParse(params);
                command = new UpdateCommand(arraysOfParams, params, username);
                break;
            default:
                throw new InvalidNameException();
        }
        return command;
    }
}

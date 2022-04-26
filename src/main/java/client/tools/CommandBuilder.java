package client.tools;

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

    public Command build(String arraysOfParams, String params, DatagramSocket clientSocket) throws TooManyArgsException, NonArgsExeption, InvalidNameException, ExecCommException, IOException {
        Command command = null;
        switch (arraysOfParams) {
            case ("show"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new ShowCommand(arraysOfParams, params);
                break;
            case ("clear"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new ClearCommand(arraysOfParams, params);
                break;
            case ("group_counting_by_id"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new GroupCountingIdCommand(arraysOfParams, params);
                break;
            case ("help"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new HelpCommand(arraysOfParams, params);
                break;
            case ("info"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new InfoCommand(arraysOfParams, params);
                break;
            case ("print_descending"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new PrintDescendingCommand(arraysOfParams, params);
                break;
            case ("history"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new HistoryCommand(arraysOfParams, params);
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
                command = new MpaaFilterCommand(arraysOfParams, params);
                break;
            case ("remove_by_id"):
                if (params.equals("")){throw new NonArgsExeption();}
                RemoveByIdCommand.tryParse(params);
                command = new RemoveByIdCommand(arraysOfParams, params);
                break;
            case ("add"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new AddCommand(arraysOfParams, params);
                break;
            case ("remove_greater"):
                if (!params.equals("")){throw new TooManyArgsException();}
                command = new RemoveGreaterCommand(arraysOfParams, params);
                break;
            case ("insert_at"):
                if (params.equals("")){throw new NonArgsExeption();}
                InsertAtCommand.tryParse(params);
                command = new InsertAtCommand(arraysOfParams, params);
                break;
            case ("update"):
                if (params.equals("")){throw new NonArgsExeption();}
                UpdateCommand.tryParse(params);
                command = new UpdateCommand(arraysOfParams, params);
                break;
            default:
                throw new InvalidNameException();
        }
        return command;
    }
}

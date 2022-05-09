package clientAndServer.tools.worksWithCommands;

import clientAndServer.commands.Command;
import clientAndServer.exeptions.InvalidNameException;
import clientAndServer.exeptions.NonArgsExeption;
import clientAndServer.exeptions.TooManyArgsException;

import java.net.DatagramPacket;

public class Invoker {

    public void findCommand(String name, String params) throws InvalidNameException, NonArgsExeption, TooManyArgsException {
        if (CommandManager.getCommands().get(name)==null) throw new InvalidNameException();
        else if ((CommandManager.getCommands().get(name).isWithArgs()==true)&&(params=="")){throw new NonArgsExeption();}
        else if ((CommandManager.getCommands().get(name).isWithArgs()==false)&&(params!="")){throw new TooManyArgsException();}
        Command command = CommandManager.getCommands().get(name);
        command.setParams(params);
        execute(command);
    }

    public void execute(Command command){
        if (command.isWithElement()&&!command.isWithArgs()){
            CommandManager.getCommands().get(command.getName()).execute(command.getMovie(), command.getUsername());
        }
        else if (command.isWithArgs()&&command.isWithElement()){
            CommandManager.getCommands().get(command.getName()).execute(command.getParams(), command.getMovie(), command.getUsername());
        }
        else {
            CommandManager.getCommands().get(command.getName()).execute(command.getParams(), command.getUsername(), command.getPassword());
        }
    }
}

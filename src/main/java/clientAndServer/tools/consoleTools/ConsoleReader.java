package clientAndServer.tools.consoleTools;

import client.Client;
import client.tools.CommandBuilder;
import clientAndServer.commands.Command;
import clientAndServer.commands.commandsClasses.withArgs.MpaaFilterCommand;
import clientAndServer.commands.commandsClasses.withArgs.RemoveByIdCommand;
import clientAndServer.commands.commandsClasses.withArgsElems.InsertAtCommand;
import clientAndServer.commands.commandsClasses.withArgsElems.UpdateCommand;
import clientAndServer.commands.commandsClasses.withElements.AddCommand;
import clientAndServer.commands.commandsClasses.withElements.RemoveGreaterCommand;
import clientAndServer.commands.commandsClasses.withoutAll.*;
import clientAndServer.exeptions.ExecCommException;
import clientAndServer.exeptions.NonArgsExeption;
import client.tools.FilesSafe;
import client.tools.Scripter;
import lombok.Getter;
import client.tools.ClientSender;
import clientAndServer.exeptions.InvalidNameException;

import clientAndServer.exeptions.TooManyArgsException;
import lombok.Setter;


import java.io.*;
import java.net.DatagramSocket;
import java.util.Scanner;

public class ConsoleReader {
    @Getter @Setter
    private static boolean toExit = false;
    private String[] arraysOfParams;
    @Setter
    private static boolean runnerFlag=true;
    public String[] getArrayOfParams(){
        return this.arraysOfParams;
    }

    public void run(DatagramSocket clientSocket) throws InvalidNameException, NonArgsExeption, TooManyArgsException, IllegalArgumentException, IOException, ExecCommException {
        ClientSender packetsSender = new ClientSender();
        Scanner scanner = new Scanner(System.in);
        CommandBuilder commandBuilder = new CommandBuilder();
        if(scanner.hasNext()){
            String consoleLine = scanner.nextLine();
            arraysOfParams = consoleLine.split(" +");
            String[] params = {""};
            if (arraysOfParams.length > 1) {
                params = new String[arraysOfParams.length - 1];
                System.arraycopy(arraysOfParams,1,params,0,arraysOfParams.length - 1);
            }
            if (params.length<=1) {
                Command command = commandBuilder.build(arraysOfParams[0], params[0], clientSocket, Client.getUsername(), Client.getPassword());
                if (command != null) {
                    packetsSender.send(clientSocket, command);
                }
                if (arraysOfParams[0].equals("execute_script")) throw new ExecCommException();
            }
            else{
                throw new TooManyArgsException();
            }
        }
    }
}

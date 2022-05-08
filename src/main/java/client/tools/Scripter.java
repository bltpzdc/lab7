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

import java.io.*;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class Scripter {
    private List<String> commandsList = new ArrayList<>();

    public void read(String fileName){
        FilesSafe.put(fileName);
        File file = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String a;
            while ((a = reader.readLine()) != null) {
                commandsList.add(a);
            }
        } catch (IOException e) {
            System.out.println("Invalid name of file");
        }
    }

    public void executeScript(DatagramSocket clientSocket) throws TooManyArgsException, ExecCommException, InvalidNameException, IOException, NonArgsExeption {
        ClientReceiver receiver = new ClientReceiver();
        ClientSender packetsSender = new ClientSender();
        CommandBuilder commandBuilder = new CommandBuilder();
        for (String com:commandsList){
            String[] arraysOfParams;
            arraysOfParams = com.split(" +");
            String[] params = {""};
            if (arraysOfParams.length > 1) {
                params = new String[arraysOfParams.length - 1];
                System.arraycopy(arraysOfParams,1,params,0,arraysOfParams.length - 1);
            }
            if (params.length<=1) {
                Command command = commandBuilder.build(arraysOfParams[0], params[0], clientSocket, Client.getUsername(), Client.getPassword());
                if (command != null) {
                    packetsSender.send(clientSocket, command);
                    try{
                        receiver.receive(clientSocket);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if (arraysOfParams[0].equals("execute_script")) throw new ExecCommException();
            }
            else{
                throw new TooManyArgsException();
            }
        }
        FilesSafe.pop();
    }
}

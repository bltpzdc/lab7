package client.tools;

import clientAndServer.commands.commandsClasses.withArgs.MpaaFilterCommand;
import clientAndServer.commands.commandsClasses.withArgs.RemoveByIdCommand;
import clientAndServer.commands.commandsClasses.withArgsElems.InsertAtCommand;
import clientAndServer.commands.commandsClasses.withArgsElems.UpdateCommand;
import clientAndServer.commands.commandsClasses.withElements.AddCommand;
import clientAndServer.commands.commandsClasses.withElements.RemoveGreaterCommand;
import clientAndServer.commands.commandsClasses.withoutAll.*;
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

    public void executeScript(DatagramSocket clientSocket){
        int sch = 0;
        ClientReceiver receiver = new ClientReceiver();
        ClientSender packetsSender = new ClientSender();
        for (String command:commandsList){
            String[] arraysOfParams;
            arraysOfParams = command.split(" +");
            String[] params = {""};
            if (arraysOfParams.length > 1) {
                params = new String[arraysOfParams.length - 1];
                System.arraycopy(arraysOfParams,1,params,0,arraysOfParams.length - 1);
            }
            if (params.length<=1) {
                try {
                    switch (arraysOfParams[0]){
                        case ("show"):
                            ShowCommand showCommand = new ShowCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket, showCommand);
                            break;
                        case ("clear"):
                            ClearCommand clearCommand = new ClearCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,clearCommand);
                            break;
                        case ("group_counting_by_id"):
                            GroupCountingIdCommand groupCountingIdCommand = new GroupCountingIdCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,groupCountingIdCommand);
                            break;
                        case ("help"):
                            HelpCommand helpCommand = new HelpCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,helpCommand);
                            break;
                        case ("info"):
                            InfoCommand infoCommand = new InfoCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,infoCommand);
                            break;
                        case ("print_descending"):
                            PrintDescendingCommand printDescendingCommand = new PrintDescendingCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,printDescendingCommand);
                            break;
                        case ("history"):
                            HistoryCommand historyCommand= new HistoryCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,historyCommand);
                            break;
                        case ("exit"):
                            ConsoleReader.setToExit(true);
                            break;
                        case ("execute_script"):
                            if (!FilesSafe.getFilesList().contains(params[0])) {
                                Scripter scripter = new Scripter();
                                scripter.read(params[0]);
                                scripter.executeScript(clientSocket);
                            }
                            else{
                                System.out.println("Recursion found");
                            }
                            break;
                        case ("filter_less_than_mpaa_rating"):
                            MpaaFilterCommand mpaaFilterCommand = new MpaaFilterCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,mpaaFilterCommand);
                            break;
                        case ("remove_by_id"):
                            RemoveByIdCommand removeByIdCommand = new RemoveByIdCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,removeByIdCommand);
                            break;
                        case ("add"):
                            AddCommand addCommand = new AddCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,addCommand);
                            break;
                        case ("remove_greater"):
                            RemoveGreaterCommand removeGreaterCommand = new RemoveGreaterCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,removeGreaterCommand);
                            break;
                        case ("insert_at"):
                            InsertAtCommand insertAtCommand = new InsertAtCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,insertAtCommand);
                            break;
                        case ("update"):
                            UpdateCommand updateCommand = new UpdateCommand(arraysOfParams[0], params[0]);
                            packetsSender.send(clientSocket,updateCommand);
                            break;
                        default:
                            sch++;
                            throw new InvalidNameException();
                    }
                } catch (InvalidNameException | NonArgsExeption | TooManyArgsException | IllegalArgumentException | IOException e) {
                    System.out.println(e.getMessage());
                    sch++;
                }
                if (arraysOfParams[0].equals("execute_script")) sch++;
            }
            else{
                sch++;
                System.out.println("Too many parameters. Use \"help\" to get a list of available commands.");
            }
        }
        for (int i=0; i<commandsList.size()-sch;i++){
            try {
                receiver.receive(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FilesSafe.pop();
    }
}

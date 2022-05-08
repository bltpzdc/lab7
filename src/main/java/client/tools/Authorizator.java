package client.tools;

import client.Client;
import clientAndServer.commands.Command;
import clientAndServer.exeptions.ExecCommException;
import clientAndServer.exeptions.InvalidNameException;
import clientAndServer.exeptions.NonArgsExeption;
import clientAndServer.exeptions.TooManyArgsException;
import lombok.Getter;
import lombok.Setter;

import java.io.Console;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Objects;
import java.util.Scanner;

public class Authorizator {
    @Setter
    private static boolean autFlag = false;
    public void authorize(DatagramSocket clientSocket) throws InvalidNameException, ExecCommException, TooManyArgsException, NonArgsExeption, IOException {
        Scanner scanner = new Scanner(System.in);
        CommandBuilder builder = new CommandBuilder();
        ClientSender sender = new ClientSender();
        ClientReceiver receiver = new ClientReceiver();
        System.out.println("Hello!");
        while (!autFlag) {
            System.out.println("Write \"log_in\" to log in or \"register\" to create new account.");
            String choose = String.join(" ",scanner.nextLine().split(" +"));
            System.out.println(choose);
            System.out.println("Insert login:");
            String username = scanner.nextLine();
            System.out.println("Insert password:");
            String password = scanner.nextLine();
            if ((Objects.equals(choose, "log_in"))||(Objects.equals(choose, "register"))) {
                Command command = builder.build(choose, "", clientSocket, username, password);
                sender.send(clientSocket, command);
                receiver.receive(clientSocket);
                Client.setUsername(username);
                Client.setPassword(password);
                Client.setNeedToAuthorize(false);
            }
            else System.out.println("Invalid command. Please, try again.");
        }
    }
}

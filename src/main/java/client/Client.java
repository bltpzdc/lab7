package client;

import client.tools.Authorizator;
import client.tools.ClientReceiver;
import clientAndServer.exeptions.ExecCommException;
import clientAndServer.exeptions.InvalidNameException;
import clientAndServer.exeptions.NonArgsExeption;
import clientAndServer.exeptions.TooManyArgsException;
import clientAndServer.tools.consoleTools.ConsoleReader;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.*;
import java.util.Map;

public class Client {
    @Getter @Setter
    private static String username;
    @Getter @Setter
    private static String password;
    @Setter
    private static boolean needToAuthorize = true;
    public static void main(String[] args) throws SocketException {
        ConsoleReader reader = new ConsoleReader();
        DatagramSocket clientSocket = new DatagramSocket();
        ClientReceiver receiver = new ClientReceiver();
        Authorizator authorizator = new Authorizator();
        while (true) {
            try {
                if (needToAuthorize) authorizator.authorize(clientSocket);
                reader.run(clientSocket);
                if (ConsoleReader.isToExit())
                    break;
                if (needToAuthorize) continue;
                receiver.receive(clientSocket);

            } catch (IllegalArgumentException e) {
                System.out.println("Illegal argument");
            } catch (InvalidNameException e) {
                System.out.println("Invalid name of command. Use help for list of available commands.");
            } catch (NonArgsExeption e) {
                System.out.println("You did not enter the argument");
            } catch (TooManyArgsException e) {
                System.out.println("You entered more arguments that we need.");
            } catch (IOException e) {
                System.out.println("Server is not available now.");
            } catch (ExecCommException ignored){
            }
        }
        }

    }


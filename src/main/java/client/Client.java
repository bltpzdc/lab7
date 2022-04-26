package client;

import client.tools.ClientReceiver;
import clientAndServer.exeptions.ExecCommException;
import clientAndServer.exeptions.InvalidNameException;
import clientAndServer.exeptions.NonArgsExeption;
import clientAndServer.exeptions.TooManyArgsException;
import clientAndServer.tools.consoleTools.ConsoleReader;

import java.io.IOException;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        ConsoleReader reader = new ConsoleReader();
        DatagramSocket clientSocket = new DatagramSocket();
        ClientReceiver receiver = new ClientReceiver();
        while (true){
            try {
                reader.run(clientSocket);
                if (ConsoleReader.isToExit())
                    break;
                receiver.receive(clientSocket);
            } catch (IllegalArgumentException e){
                System.out.println("Illegal argument");
            }
            catch(InvalidNameException e){
                System.out.println("Invalid name of command. Use help for list of available commands.");
            }
            catch (NonArgsExeption e){
                System.out.println("You did not enter the argument");
            }
             catch (TooManyArgsException e){
                 System.out.println("You entered more arguments that we need.");
             }
            catch (IOException | ExecCommException ignored){
            }

            //receiver.receive(clientSocket);
        }

    }
}

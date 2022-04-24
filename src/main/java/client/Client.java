package client;

import client.tools.ClientReceiver;
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
            }
            catch (Exception e){
                continue;
            }
            try{receiver.receive(clientSocket);}
            catch (SocketTimeoutException e){
                System.out.println("Server is not available now.");
                break;
            }
        }

    }
}

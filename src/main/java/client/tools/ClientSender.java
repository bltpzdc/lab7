package client.tools;

import clientAndServer.commands.Command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class ClientSender {
    private static final int SERVER_PORT=8000;
    public void send(DatagramSocket clientSocket, Command command) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)){
            objectOutputStream.writeObject(command);
            DatagramPacket packet = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.toByteArray().length, InetAddress.getByName("localhost"), SERVER_PORT);
            clientSocket.send(packet);
            clientSocket.setSoTimeout(3000);

            System.out.println("Command "+command.getName()+" sent.\n");
        }
        catch (SocketTimeoutException e){
            System.out.println("Server is not available now.");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

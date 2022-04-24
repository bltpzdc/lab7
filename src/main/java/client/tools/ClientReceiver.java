package client.tools;

import server.tools.ServerAnswer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class ClientReceiver {
    public void receive(DatagramSocket clientSocket) throws IOException {
        byte[] receiveArray = new byte[8096];
        DatagramPacket receivePacket = new DatagramPacket(receiveArray, receiveArray.length);
        try {
            clientSocket.receive(receivePacket);
            System.out.println("Answer from server received:");
        }
        catch (SocketTimeoutException e){
            System.out.println("Server is not available now.");
        }
        try(ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(receiveArray))){
            ServerAnswer serverAnswer = (ServerAnswer) ois.readObject();
            System.out.println(serverAnswer.getMessage());
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
    }
}

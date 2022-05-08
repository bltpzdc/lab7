package client.tools;

import server.tools.ServerAnswer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Objects;

public class ClientReceiver {
    public void receive(DatagramSocket clientSocket) throws IOException {
        byte[] receiveArray = new byte[8096];
        DatagramPacket receivePacket = new DatagramPacket(receiveArray, receiveArray.length);
            clientSocket.receive(receivePacket);
            System.out.println("Answer from server received:");
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(receiveArray))){
            ServerAnswer serverAnswer = (ServerAnswer) objectInputStream.readObject();
            System.out.println(serverAnswer.getMessage());
            if (Objects.equals(serverAnswer.getMessage(), "You are authorized. Write sign_out to exit from account.")) Authorizator.setAutFlag(true);
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
    }
}

package server.tools;

import server.tools.ServerAnswer;
import server.tools.ServerReceiver;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

public class ServerSender {
    public void send(ServerAnswer answer, String username){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)){
            objectOutputStream.writeObject(answer);
            DatagramPacket sendPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.toByteArray().length, PacketsSafe.get(username).getAddress(), PacketsSafe.get(username).getPort());
            ServerReceiver.getDatagramSocket().send(sendPacket);
            System.out.println("Answer sent");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

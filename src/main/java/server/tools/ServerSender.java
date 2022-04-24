package server.tools;

import server.tools.ServerAnswer;
import server.tools.ServerReceiver;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

public class ServerSender {
    public void send(ServerAnswer answer){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(ObjectOutputStream ous = new ObjectOutputStream(baos)){
            ous.writeObject(answer);
            DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, ServerReceiver.getDatagramPacket().getAddress(), ServerReceiver.getDatagramPacket().getPort());
            ServerReceiver.getDatagramSocket().send(sendPacket);
            System.out.println("Answer sent");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

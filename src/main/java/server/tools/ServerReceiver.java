package server.tools;

import clientAndServer.commands.Command;
import lombok.Getter;
import clientAndServer.tools.insideCommands.HistorySaver;
import clientAndServer.tools.worksWithCommands.Invoker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerReceiver {
    @Getter
    private static DatagramPacket datagramPacket;
    @Getter
    private static DatagramSocket datagramSocket;

    public void receive(DatagramSocket serverSocket) throws IOException {

        Invoker invoker = new Invoker();
        byte[] receiveData = new byte[8096];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        datagramPacket = receivePacket;
        datagramSocket = serverSocket;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(receiveData))) {
            Command command = (Command) ois.readObject();
            System.out.println("Command "+ command.getName()+" received.");
            invoker.execute(command);
            HistorySaver saver = new HistorySaver();
            saver.save(command.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

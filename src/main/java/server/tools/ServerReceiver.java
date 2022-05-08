package server.tools;

import clientAndServer.commands.Command;
import clientAndServer.tools.insideCommands.HistorySaver;
import lombok.Getter;
import clientAndServer.tools.worksWithCommands.Invoker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerReceiver implements Runnable{
    @Getter
    private static DatagramSocket datagramSocket;
    private static ExecutorService poolExecutor = Executors.newCachedThreadPool();
    private static HistorySaver saver = new HistorySaver();

    public ServerReceiver(DatagramSocket socket){
        datagramSocket = socket;
    }

    public void receive() throws IOException {
        Invoker invoker = new Invoker();
        byte[] receiveData = new byte[8096];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        datagramSocket.receive(receivePacket);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(receiveData))) {
            Command command = (Command) objectInputStream.readObject();
            System.out.println("Command "+ command.getName()+" received.");
            poolExecutor.execute(() -> invoker.execute(command, receivePacket));
            /*invoker.execute(command, receivePacket);*/
            saver.save(command.getName());
        } catch (Exception e) {
        }
    }

    @Override
    public void run(){
        try {
            receive();
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }
    }
}

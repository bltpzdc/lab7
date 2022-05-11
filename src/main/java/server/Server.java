package server;

import clientAndServer.tools.collectionTools.CollectionSaver;
import server.tools.DatabaseCommunicator;
import server.tools.ServerReceiver;
import clientAndServer.tools.collectionTools.CollectionLoader;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.tools.worksWithCommands.CommandManager;

import java.io.IOException;
import java.net.DatagramSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ExecutorService poolReader = Executors.newCachedThreadPool();
    private static int SERVER_PORT=8000;

    public static void main(String[] args) {
        CollectionLoader loader = new CollectionLoader();
        loader.loadDatabase();
        CollectionManager collectionManager = new CollectionManager(loader, new DatabaseCommunicator());
        CommandManager commandManager = new CommandManager(collectionManager);
        try {
            DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);
            ServerReceiver receiver = new ServerReceiver(serverSocket);
            while (true) {
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    System.out.println("Main thread was interrupted.");
                }
                poolReader.execute(receiver);
            }
        } catch (IOException e){
            System.out.println("Can not connect with client.");
        }
    }

    private static void shuttingDown(CollectionManager manager){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CollectionSaver saver = new CollectionSaver(manager);
            saver.save();
            System.out.println("Server is closed");
        }));
    }
}

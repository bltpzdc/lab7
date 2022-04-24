package server;

import server.tools.ServerReceiver;
import clientAndServer.tools.collectionTools.CollectionLoader;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.tools.worksWithCommands.CommandManager;

import java.io.IOException;
import java.net.DatagramSocket;

public class Server {
    private static int SERVER_PORT=8000;

    public static void main(String[] args) throws IOException {
        CollectionLoader loader = new CollectionLoader();
        loader.load();
        CollectionManager collectionManager = new CollectionManager(loader);
        CommandManager commandManager = new CommandManager(collectionManager);
        DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);
        while(true) {
            ServerReceiver receiver = new ServerReceiver();
            receiver.receive(serverSocket);

        }

    }
}

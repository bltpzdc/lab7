package clientAndServer.commands.commandsClasses.withArgs;

import clientAndServer.exeptions.NonArgsExeption;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;
import java.net.DatagramPacket;

public class ExecuteScript implements Command, Serializable {
    private String name="execute_script";
    @Getter
    private Movie movie;
    private CollectionManager manager;
    @Getter @Setter
    private String params;
    @Getter
    private String username;
    @Getter
    private String password;

    public ExecuteScript(CollectionManager movieList){
        this.manager=movieList;
    }
    public ExecuteScript(String name, String params, String username, String password){
        this.name=name;
        this.password = password;
        this.username = username;
        this.params=params;
    }

    @Override
    public void execute(String params, String username, String password, DatagramPacket packet) {
        manager.executeScript(params);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isWithElement() {
        return false;
    }

    @Override
    public boolean isWithArgs() {
        return true;
    }
}

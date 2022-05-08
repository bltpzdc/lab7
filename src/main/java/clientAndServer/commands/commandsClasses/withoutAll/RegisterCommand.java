package clientAndServer.commands.commandsClasses.withoutAll;

import clientAndServer.commands.Command;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.net.DatagramPacket;

public class RegisterCommand implements Command, Serializable {
    private String name="register";
    @Getter
    private Movie movie;
    private CollectionManager movieList;
    @Getter
    @Setter
    private String params;
    @Getter
    private String username;
    @Getter
    private String password;

    public RegisterCommand(CollectionManager movieList){
        this.movieList=movieList;
    }
    public RegisterCommand(String name, String params, String username, String password){
        this.name=name;
        this.password = password;
        this.username = username;
    }

    @Override
    public void execute(String params, String username, String password, DatagramPacket packet) {
        movieList.register(username, password, packet);
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
        return false;
    }
}

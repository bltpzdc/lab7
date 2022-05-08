package clientAndServer.commands.commandsClasses.withoutAll;

import clientAndServer.exeptions.TooManyArgsException;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;
import java.net.DatagramPacket;

public class ClearCommand implements Command, Serializable {
    private String name="clear";
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

    public ClearCommand(CollectionManager movieList){
        this.movieList=movieList;
    }
    public ClearCommand(String name, String params, String username, String password){
        this.name=name;
        this.password = password;
        this.username = username;
    }

    @Override
    public void execute(String params, String username, String password, DatagramPacket packet) {
        movieList.clear(username, packet);
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

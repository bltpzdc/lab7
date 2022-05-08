package clientAndServer.commands.commandsClasses.withArgs;

import clientAndServer.exeptions.NonArgsExeption;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;
import java.net.DatagramPacket;

public class RemoveByIdCommand implements Command, Serializable {
    private String name="remove_by_id";
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

    public RemoveByIdCommand(CollectionManager movieList){
        this.movieList=movieList;
    }
    public RemoveByIdCommand(String name, String params, String username, String password){
        this.name=name;
        this.password = password;
        this.username = username;
        this.params=params;
    }

    @Override
    public void execute(String params, String username, String password, DatagramPacket packet) {
        movieList.remove(tryParse(params), username, packet);
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
    public static int tryParse(String text) throws IllegalArgumentException {
        return Integer.parseInt(text);
    }
}

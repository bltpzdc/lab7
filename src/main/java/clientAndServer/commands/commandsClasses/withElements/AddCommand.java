package clientAndServer.commands.commandsClasses.withElements;

import clientAndServer.exeptions.TooManyArgsException;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.insideCommands.MovieBuilder;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;
import java.net.DatagramPacket;

public class AddCommand implements Command, Serializable {
    private CollectionManager manager;
    private String name="add";
    @Getter @Setter
    private Movie movie;
    @Getter
    @Setter
    private String params;
    @Getter
    private String username;
    @Getter
    private String password;

    public AddCommand(CollectionManager manager){
        this.manager=manager;
    }
    public AddCommand(String name, String params, String username) {
        this.name=name;
        MovieBuilder movieBuilder = new MovieBuilder();
        movie = movieBuilder.build();
        this.username = username;
    }

    @Override
    public void execute(Movie movie, String username){
        manager.add(movie, username);
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isWithElement() {
        return true;
    }

    @Override
    public boolean isWithArgs() {
        return false;
    }
}

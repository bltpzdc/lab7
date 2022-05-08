package clientAndServer.commands.commandsClasses.withArgsElems;

import clientAndServer.exeptions.NonArgsExeption;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.insideCommands.MovieBuilder;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;
import java.net.DatagramPacket;

public class UpdateCommand implements Command, Serializable {
    private CollectionManager movieList;
    private String name="update";
    @Getter
    private Movie movie;
    @Getter
    @Setter
    private String params;
    @Getter
    private String username;
    @Getter
    private String password;

    public UpdateCommand(CollectionManager movieList){
        this.movieList=movieList;
    }
    public UpdateCommand(String name, String params, String username){
        this.name=name;
        this.params = params;
        this.username = username;
        MovieBuilder movieBuilder = new MovieBuilder();
        movie = movieBuilder.build();
    }

    @Override
    public void execute(String params, Movie movie, String username, DatagramPacket packet) {
        movieList.update(tryParse(params), movie, username, packet);
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
        return true;
    }

    public static int tryParse(String text) throws IllegalArgumentException {
        return Integer.parseInt(text);
    }
}

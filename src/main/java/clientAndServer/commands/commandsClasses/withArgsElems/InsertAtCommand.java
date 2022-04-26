package clientAndServer.commands.commandsClasses.withArgsElems;

import clientAndServer.exeptions.NonArgsExeption;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.insideCommands.MovieBuilder;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;

public class InsertAtCommand implements Command, Serializable {
    private String name="insert_at";
    @Getter
    private Movie movie;
    private CollectionManager manager;
    @Getter
    @Setter
    private String params;

    public InsertAtCommand(CollectionManager manager){
        this.manager=manager;
    }
    public InsertAtCommand(String name, String params){
        this.name=name;
        this.params = params;
        MovieBuilder movieBuilder = new MovieBuilder();
        movie = movieBuilder.build();
    }

    @Override
    public void execute(String params, Movie movie) {
        manager.insertAt(tryParse(params), movie);
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

package clientAndServer.commands.commandsClasses.withElements;

import clientAndServer.exeptions.TooManyArgsException;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.insideCommands.MovieBuilder;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;

public class RemoveGreaterCommand implements Command, Serializable {
    private String name="remove_greater";
    @Getter
    private Movie movie;
    private CollectionManager manager;
    @Getter
    @Setter
    private String params;

    public RemoveGreaterCommand(CollectionManager manager){
        this.manager=manager;
    }
    public RemoveGreaterCommand(String name, String params) throws TooManyArgsException {
        this.name=name;
        if (!params.equals("")){throw new TooManyArgsException();
        }
        MovieBuilder movieBuilder = new MovieBuilder();
        movie = movieBuilder.build();
    }

    @Override
    public void execute(Movie movie){
        manager.removeGreater(movie);
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

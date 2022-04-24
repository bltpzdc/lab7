package clientAndServer.commands.commandsClasses.withElements;

import clientAndServer.exeptions.TooManyArgsException;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.insideCommands.MovieBuilder;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;

public class AddCommand implements Command, Serializable {
    private CollectionManager manager;
    private String name="add";
    @Getter @Setter
    private Movie movie;
    @Getter
    @Setter
    private String params;

    public AddCommand(CollectionManager manager){
        this.manager=manager;
    }
    public AddCommand(String name, String params) throws TooManyArgsException {
        this.name=name;
        if (!params.equals("")){throw new TooManyArgsException();
        }
        MovieBuilder movieBuilder = new MovieBuilder();
        movie = movieBuilder.build();
    }

    @Override
    public void execute(Movie movie){
        manager.add(movie);
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

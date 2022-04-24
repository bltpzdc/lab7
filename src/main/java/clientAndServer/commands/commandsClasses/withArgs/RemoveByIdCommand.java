package clientAndServer.commands.commandsClasses.withArgs;

import clientAndServer.exeptions.NonArgsExeption;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;

public class RemoveByIdCommand implements Command, Serializable {
    private String name="remove_by_id";
    @Getter
    private Movie movie;
    private CollectionManager movieList;
    @Getter
    @Setter
    private String params;

    public RemoveByIdCommand(CollectionManager movieList){
        this.movieList=movieList;
    }

    public RemoveByIdCommand(String name, String params) throws NonArgsExeption, IllegalArgumentException{
        this.name=name;
        this.params = params;
        if (params.equals("")){throw new NonArgsExeption();
        }
        tryParse(params);
    }

    @Override
    public void execute(String params) {
        movieList.remove(tryParse(params));
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
    public int tryParse(String text){
        try{
            return Integer.parseInt(text);
        }
        catch(IllegalArgumentException e){
            System.out.println("Invalid argument");
            throw new IllegalArgumentException();
        }
    }
}

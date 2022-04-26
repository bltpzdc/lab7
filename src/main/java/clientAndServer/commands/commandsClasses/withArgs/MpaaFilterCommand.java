package clientAndServer.commands.commandsClasses.withArgs;

import clientAndServer.exeptions.NonArgsExeption;
import lombok.Getter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;
import clientAndServer.startingData.MpaaRating;

import java.io.Serializable;

public class MpaaFilterCommand implements Command, Serializable {
    private String name="filter_less_than_mpaa_rating";
    @Getter
    private Movie movie;
    private CollectionManager manager;
    @Getter
    private String params;

    public void setParams(String params){
        this.params=params;
        tryParse(params);
    }

    public MpaaFilterCommand(String name, String params){
        this.name=name;
        this.params = params;
    }

    public MpaaFilterCommand(CollectionManager manager){
        this.manager=manager;
    }

    @Override
    public void execute(String params) {
        manager.mpaaRatingFilter(tryParse(params));
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
    public static MpaaRating tryParse(String text) throws IllegalArgumentException {
        return MpaaRating.valueOf(text);
    }
}

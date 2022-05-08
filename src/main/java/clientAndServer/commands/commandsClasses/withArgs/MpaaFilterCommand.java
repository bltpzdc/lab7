package clientAndServer.commands.commandsClasses.withArgs;

import clientAndServer.exeptions.NonArgsExeption;
import lombok.Getter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;
import clientAndServer.startingData.MpaaRating;

import java.io.Serializable;
import java.net.DatagramPacket;

public class MpaaFilterCommand implements Command, Serializable {
    private String name="filter_less_than_mpaa_rating";
    @Getter
    private Movie movie;
    private CollectionManager manager;
    @Getter
    private String params;
    @Getter
    private String username;
    @Getter
    private String password;

    public void setParams(String params){
        this.params=params;
        tryParse(params);
    }

    public MpaaFilterCommand(CollectionManager movieList){
        this.manager=movieList;
    }
    public MpaaFilterCommand(String name, String params, String username, String password){
        this.name=name;
        this.password = password;
        this.username = username;
        this.params=params;
    }

    @Override
    public void execute(String params, String username, String password, DatagramPacket packet) {
        manager.mpaaRatingFilter(tryParse(params), packet);
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

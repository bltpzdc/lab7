package clientAndServer.commands.commandsClasses.withoutAll;

import clientAndServer.exeptions.TooManyArgsException;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;

public class ShowCommand implements Command, Serializable {
    private String name="show";
    @Getter
    private Movie movie;
    private final boolean isWithElement=false;
    private CollectionManager manager;
    @Getter
    @Setter
    private String params;

    public ShowCommand(CollectionManager manager){
        this.manager=manager;
    }
    public ShowCommand(String name, String params) throws TooManyArgsException {
        this.name=name;
        if (!params.equals("")){throw new TooManyArgsException();
        }
    }

    @Override
    public void execute(String params) {
        manager.show();
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

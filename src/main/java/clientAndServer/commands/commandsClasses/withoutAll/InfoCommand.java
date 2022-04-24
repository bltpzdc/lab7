package clientAndServer.commands.commandsClasses.withoutAll;

import clientAndServer.exeptions.TooManyArgsException;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;

public class InfoCommand implements Command, Serializable {
    private String name = "info";
    @Getter
    private Movie movie;
    private CollectionManager manager;
    @Getter
    @Setter
    private String params;

    public InfoCommand(CollectionManager manager){
        this.manager=manager;
    }
    public InfoCommand(String name, String params) throws TooManyArgsException {
        this.name=name;
        if (!params.equals("")){throw new TooManyArgsException();
        }
    }

    @Override
    public void execute(String params) {
        manager.info();
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

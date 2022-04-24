package clientAndServer.commands.commandsClasses.withoutAll;

import clientAndServer.exeptions.TooManyArgsException;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;

public class PrintDescendingCommand implements Command, Serializable {
    private String name="print_descending";
    @Getter
    private Movie movie;
    private CollectionManager manager;
    @Getter
    @Setter
    private String params;

    public PrintDescendingCommand(CollectionManager manager){
        this.manager=manager;
    }
    public PrintDescendingCommand(String name, String params) throws TooManyArgsException {
        this.name=name;
        if (!params.equals("")){throw new TooManyArgsException();
        }
    }

    @Override
    public void execute(String params) {
        manager.printDescending();
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

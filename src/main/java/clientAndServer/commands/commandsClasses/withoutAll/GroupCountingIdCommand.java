package clientAndServer.commands.commandsClasses.withoutAll;

import clientAndServer.exeptions.TooManyArgsException;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;

public class GroupCountingIdCommand implements Command, Serializable {
    private String name="group_counting_by_id";
    @Getter
    private Movie movie;
    private CollectionManager manager;
    @Getter
    @Setter
    private String params;

    public GroupCountingIdCommand(CollectionManager manager){
        this.manager=manager;
    }
    public GroupCountingIdCommand(String name, String params) throws TooManyArgsException {
        this.name=name;
        if (!params.equals("")){throw new TooManyArgsException();
        }
    }

    /*@Override
    public void setParams(String params) {
        this.params = params;
    }*/

    @Override
    public void execute(String params) {
        manager.groupCountingId();
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

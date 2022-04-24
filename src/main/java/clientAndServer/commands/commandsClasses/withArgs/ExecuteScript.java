package clientAndServer.commands.commandsClasses.withArgs;

import clientAndServer.exeptions.NonArgsExeption;
import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

import java.io.Serializable;

public class ExecuteScript implements Command, Serializable {
    private String name="execute_script";
    @Getter
    private Movie movie;
    private CollectionManager manager;
    @Getter @Setter
    private String params;

    public ExecuteScript(CollectionManager manager){
        this.manager=manager;
    }
    public ExecuteScript(String name, String params) throws NonArgsExeption {
        this.name=name;
        this.params = params;
        if (params.equals("")){throw new NonArgsExeption();
        }
    }

    @Override
    public void execute(String params) {
        manager.executeScript(params);
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
}

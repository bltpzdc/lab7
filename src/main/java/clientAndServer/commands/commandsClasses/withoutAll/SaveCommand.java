package clientAndServer.commands.commandsClasses.withoutAll;

import lombok.Getter;
import lombok.Setter;
import clientAndServer.startingData.Movie;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;

public class SaveCommand implements Command {
    private String name="save";
    @Getter
    private Movie movie;
    private CollectionManager manager;
    @Getter
    @Setter
    private String params;

    public SaveCommand(CollectionManager manager){
        this.manager=manager;
    }

    @Override
    public void execute(String params) {
        manager.save();
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

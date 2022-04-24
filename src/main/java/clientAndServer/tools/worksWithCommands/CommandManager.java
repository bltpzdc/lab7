package clientAndServer.tools.worksWithCommands;

import clientAndServer.commands.commandsClasses.withoutAll.*;
import clientAndServer.tools.collectionTools.CollectionLoader;
import clientAndServer.tools.collectionTools.CollectionManager;
import clientAndServer.commands.Command;
import clientAndServer.commands.commandsClasses.withArgs.ExecuteScript;
import clientAndServer.commands.commandsClasses.withArgs.MpaaFilterCommand;
import clientAndServer.commands.commandsClasses.withArgs.RemoveByIdCommand;
import clientAndServer.commands.commandsClasses.withArgsElems.InsertAtCommand;
import clientAndServer.commands.commandsClasses.withArgsElems.UpdateCommand;
import clientAndServer.commands.commandsClasses.withElements.AddCommand;
import clientAndServer.commands.commandsClasses.withElements.RemoveGreaterCommand;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    @Getter
    private static final Map<String, Command> commands= new HashMap<>();
    private CollectionLoader loader;

    private void registerCommand(Command command){
        commands.put(command.getName(), command);
    }

    public CommandManager(CollectionManager manager) {
        registerCommand(new HelpCommand(manager));
        registerCommand(new InfoCommand(manager));
        registerCommand(new ShowCommand(manager));
        registerCommand(new AddCommand(manager));
        registerCommand(new UpdateCommand(manager));
        registerCommand(new RemoveByIdCommand(manager));
        registerCommand(new ClearCommand(manager));
        registerCommand(new SaveCommand(manager));
        registerCommand(new InsertAtCommand(manager));
        registerCommand(new RemoveGreaterCommand(manager));
        registerCommand(new MpaaFilterCommand(manager));
        registerCommand(new PrintDescendingCommand(manager));
        registerCommand(new ExitCommand(manager));
        registerCommand(new HistoryCommand(manager));
        registerCommand(new GroupCountingIdCommand(manager));
        registerCommand(new ExecuteScript(manager));
    }
}

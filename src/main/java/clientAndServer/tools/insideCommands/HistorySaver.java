package clientAndServer.tools.insideCommands;

import java.util.ArrayList;
import java.util.List;

public class HistorySaver {
    private List<String> list = new ArrayList<>();

    public void save(String commandName){
        list = HistorySafe.getHistory();
        if (list.size()<8){
            list.add(commandName);
        }
        else{
            list.remove(0);
            list.add(commandName);
        }
        HistorySafe.setHistory(list);
    }
}

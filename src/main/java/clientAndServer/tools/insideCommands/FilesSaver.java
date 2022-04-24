package clientAndServer.tools.insideCommands;

import java.util.List;

public class FilesSaver {
    public void save(String fileName){
        List<String> list = FilesSafe.getFileNamesList();
        list.add(fileName);
        FilesSafe.setFileNamesList(list);
    }
}

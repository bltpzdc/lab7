package clientAndServer.tools.insideCommands;

import java.util.Objects;

public class FileChecker {
    public boolean check(String fileName){
        int sch=0;
        for (String i: FilesSafe.getFileNamesList()){
            if (Objects.equals(fileName, i)){sch++;}
        }
        if (sch==0){return true;}
        else{return false;}
    }
}

package clientAndServer.tools.insideCommands;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class HistorySafe {
    @Getter @Setter
    private static List<String> history = new ArrayList<>();

    @Override
    public String toString() {
        if (history.isEmpty()){
            return "You have not entered commands yet";
        }
        else{
            String outPut="Last commands (from 1 to 8):\n";
            for (String i:history){
                outPut = outPut+history+" ";
            }
            return outPut;
        }
    }
}

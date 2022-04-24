package clientAndServer.tools.insideCommands;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class FilesSafe {
    @Getter @Setter
    private static List<String> fileNamesList = new ArrayList<>();
}

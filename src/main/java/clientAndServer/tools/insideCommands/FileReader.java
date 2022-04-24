package clientAndServer.tools.insideCommands;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    @Getter
    private final List<String> commandsList = new ArrayList<>();

    public void read(String fileName){
        File file = new File(fileName);
        try {
            FileInputStream f = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(f));
            String a;
            while ((a = reader.readLine())!=null){
                commandsList.add(a);
            }
            f.close();
        }
        catch (IOException e){
            System.out.println("Invalid name of file.");
        }
    }
}

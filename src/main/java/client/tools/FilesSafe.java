package client.tools;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class FilesSafe {
    @Getter @Setter
    private static List<String> filesList = new ArrayList<>();

    public static void put(String file){
        filesList.add(file);
    }

    public static void pop(){
        filesList.remove(filesList.size()-1);
    }
}

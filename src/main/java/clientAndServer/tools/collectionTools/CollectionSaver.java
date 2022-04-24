package clientAndServer.tools.collectionTools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import clientAndServer.startingData.CustomVector;
import clientAndServer.startingData.Movie;

import java.io.*;

public class CollectionSaver {
    private CollectionManager manager;
    private CustomVector<Movie> movieList;

    public CollectionSaver(CollectionManager manager){
        this.manager=manager;
        movieList = manager.getMovieList();
    }

    public void save() {
        try {
            var env = System.getenv();
            File fileName = new File("src\\main\\resources\\" + env.get("PROCESSOR_ARCHITECTURE") + ".json");
            Gson gson = new GsonBuilder()
                    .setDateFormat("dd/MM/yyyy").create();
            String json = gson.toJson(movieList);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
                writer.write(json);
                writer.close();
            System.out.println("Collection has been uploaded to the file.");
        } catch (IOException e){
            System.out.println("You have not rights to save this collection.");
        }
    }
}

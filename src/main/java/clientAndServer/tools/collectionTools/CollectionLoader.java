package clientAndServer.tools.collectionTools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import clientAndServer.startingData.CustomVector;
import clientAndServer.startingData.Movie;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionLoader {
    private CustomVector<Movie> movieList;

    public void load() {
        var env = System.getenv();
        System.out.println(env);
        File fileName = new File(env.get("COLLECTION"));
        System.out.println(fileName);
            List<String> filesLines = new ArrayList<>();
            try {
                FileInputStream stream = new FileInputStream(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String jsonHelper;
                while ((jsonHelper = reader.readLine()) != null) {
                    filesLines.add(jsonHelper);
                }
                stream.close();
                String json = String.join("", filesLines);
                Gson gson = new GsonBuilder()
                        .setDateFormat("dd/MM/yyyy").create();
                Type movieTypes = new TypeToken<CustomVector<Movie>>(){}.getType();
                this.movieList = gson.fromJson(json, movieTypes);
                Collections.sort(movieList);
            } catch (IOException e) {
                System.out.println("You have not rights to load the collection.\n" +
                        "We created new collection.");
                String json="[]";
                Gson gson = new GsonBuilder()
                        .setDateFormat("dd/MM/yyyy").create();
                Type movieTypes = new TypeToken<CustomVector<Movie>>(){}.getType();
                this.movieList = gson.fromJson(json, movieTypes);
                Collections.sort(movieList);
            }
    }

    public CustomVector<Movie> getMovieList(){
        return movieList;
    }
}

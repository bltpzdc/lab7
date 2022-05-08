package clientAndServer.tools.collectionTools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import clientAndServer.startingData.CustomVector;
import clientAndServer.startingData.Movie;
import server.tools.DatabaseCommunicator;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionLoader {
    private CustomVector<Movie> movieList = new CustomVector<>();

    public void load() {
        var env = System.getenv();
        File fileName = new File(env.get("COLLECTION"));
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

    public void loadDatabase(){
        try {
            new DatabaseCommunicator().load(movieList);
        } catch (SQLException e) {
            System.out.println("Can't connect to database.");
        } catch (ClassNotFoundException e) {
            System.out.println("No such driver to connect to database.");
        }
    }

    public CustomVector<Movie> getMovieList(){
        return movieList;
    }
}

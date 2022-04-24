package clientAndServer.startingData;


import lombok.*;

import java.io.Serializable;

@Data
@Builder
public class Movie implements Comparable<Movie>, Serializable {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private Coordinates coordinates;
    @NonNull
    private Long oscarsCount;
    @NonNull
    private MovieGenre genre;
    @NonNull
    private MpaaRating mpaaRating;
    @NonNull
    private Person director;
    @NonNull
    private java.time.LocalDate creationDate;

    public int getID(){
        return id;
    }

    @Override
    public String toString(){
        return "id: "+id+". Name: "+name+". Coordinates: "+coordinates+". Creation date: "+creationDate+". Oscars count: "+oscarsCount+". Genre: "+genre+". Mpaa rating: "+mpaaRating+". Director: "+director;
    }

    @Override
    public int compareTo(Movie o) {
         return id.compareTo(o.getID());
    }


}

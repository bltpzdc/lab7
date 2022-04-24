package clientAndServer.tools.insideCommands;

import clientAndServer.startingData.Movie;
import clientAndServer.startingData.MpaaRating;

import java.util.Comparator;

public class MpaaRatingComparator implements Comparator<Movie> {

    public int getIntValue(MpaaRating mpaaRating){
        return switch (mpaaRating) {
            case G -> 0;
            case PG -> 1;
            case PG_13 -> 2;
            case R -> 3;
        };
    }

    @Override
    public int compare(Movie o1, Movie o2) {
        return getIntValue(o1.getMpaaRating())-getIntValue(o2.getMpaaRating());
    }
}

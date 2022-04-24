package clientAndServer.commands;

import clientAndServer.startingData.Movie;

public interface Command {
    Movie getMovie();
    void setParams(String params);
    String getParams();

    default void execute(String params) {}

    default void execute(Movie movie){}
    default void execute(String params, Movie movie){}
    String getName();
    boolean isWithElement();
    boolean isWithArgs();
}

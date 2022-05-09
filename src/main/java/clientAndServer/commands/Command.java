package clientAndServer.commands;

import clientAndServer.startingData.Movie;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;

public interface Command {
    Movie getMovie();
    void setParams(String params);
    String getParams();
    String getUsername();
    String getPassword();

    default void execute(String params, String username, String password) {};

    default void execute(Movie movie, String username){};
    default void execute(String params, Movie movie, String username){};
    String getName();
    boolean isWithElement();
    boolean isWithArgs();
}

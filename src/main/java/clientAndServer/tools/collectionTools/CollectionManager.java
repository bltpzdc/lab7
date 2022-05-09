package clientAndServer.tools.collectionTools;

import clientAndServer.startingData.CustomVector;
import clientAndServer.startingData.Movie;
import clientAndServer.startingData.MpaaRating;
import clientAndServer.tools.consoleTools.ConsoleReader;
import clientAndServer.tools.insideCommands.*;
import server.Server;
import server.tools.DatabaseCommunicator;
import server.tools.ServerAnswer;
import server.tools.ServerSender;
import clientAndServer.tools.insideCommands.*;
import clientAndServer.tools.worksWithCommands.Invoker;
import clientAndServer.exeptions.InvalidNameException;
import clientAndServer.exeptions.NonArgsExeption;
import clientAndServer.exeptions.TooManyArgsException;
import lombok.Getter;
import clientAndServer.startingData.*;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CollectionManager {
    @Getter
    private CustomVector<Movie> movieList;
    private static ExecutorService poolSender = Executors.newCachedThreadPool();
    private ServerSender serverSender = new ServerSender();
    private ServerAnswer serverAnswer;
    private DatabaseCommunicator databaseCommunicator;

    public CollectionManager(CollectionLoader loader, DatabaseCommunicator databaseCommunicator) {
        movieList=loader.getMovieList();
        this.databaseCommunicator = databaseCommunicator;
    }

    public void info(String username){

        serverAnswer = new ServerAnswer( "Collection information:\n"+
                "Type: "+movieList.toString()+".\n"+
                "Initialize date: "+movieList.getInitTime()+".\n"+
                "Element count: "+movieList.size());
        poolSender.execute(() -> serverSender.send(serverAnswer, username));
    }

    public void show(String username){
        String ans="";
        if (movieList.size()==0){
            serverAnswer = new ServerAnswer("There's no any elements in collection.");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
        else{
            for (Movie i:movieList){
                ans = ans+i+"\n";
            }
            serverAnswer = new ServerAnswer(ans);
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
    }

    public synchronized void add(Movie movie, String username){
        try {
            databaseCommunicator.add(movie, username);
            movieList.clear();
            databaseCommunicator.load(movieList);
        } catch (ClassNotFoundException e) {
            serverAnswer = new ServerAnswer("Something went wrong. Check database drivers.");
            serverSender.send(serverAnswer, username);
        } catch (SQLException e) {
            serverAnswer = new ServerAnswer("Database is not available now.");
            e.printStackTrace();
            serverSender.send(serverAnswer, username);
        }
        serverAnswer = new ServerAnswer("New element has been added to collection.");
        serverSender.send(serverAnswer, username);
    }

    public synchronized void update(int id, Movie movie, String username) {
        if (id < 1){
            serverAnswer = new ServerAnswer("ID should be higher than 0.");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
        else {
            try {
                if (databaseCommunicator.canFindMovieByID(id)){
                    if (databaseCommunicator.update(id, movie, username)) {
                        serverAnswer = new ServerAnswer("Element updated.");
                        movieList.clear();
                        databaseCommunicator.load(movieList);
                    }
                    else serverAnswer = new ServerAnswer("You can not update this element because you did not create it.");
                }
                else serverAnswer = new ServerAnswer("No elements with this ID.");
            } catch (ClassNotFoundException e) {
                serverAnswer = new ServerAnswer("Something went wrong. Check database drivers.");
                poolSender.execute(() -> serverSender.send(serverAnswer, username));
            } catch (SQLException e) {
                serverAnswer = new ServerAnswer("Database is not available now.");
                poolSender.execute(() -> serverSender.send(serverAnswer, username));
            }
        }
        poolSender.execute(() -> serverSender.send(serverAnswer, username));
    }

    public synchronized void remove(int id, String username){
        if (id < 1){
            serverAnswer = new ServerAnswer("ID should be higher than 0.");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
        else {
            try {
                if (databaseCommunicator.canFindMovieByID(id)){
                    if (databaseCommunicator.deleteMovie(id, username)) {
                        serverAnswer = new ServerAnswer("Element deleted.");
                        for (Movie i : movieList) {
                            if (i.getID()==id){
                                movieList.remove(i);
                                break;
                            }
                        }
                    }
                    else serverAnswer = new ServerAnswer("You can not delete this element because you did not create it.");
                }
                else serverAnswer = new ServerAnswer("No elements with this ID.");
            } catch (ClassNotFoundException e) {
                serverAnswer = new ServerAnswer("Something went wrong. Check database drivers.");
                poolSender.execute(() -> serverSender.send(serverAnswer, username));
            } catch (SQLException e) {
                serverAnswer = new ServerAnswer("Database is not available now.");
                poolSender.execute(() -> serverSender.send(serverAnswer, username));
            }
        }
        poolSender.execute(() -> serverSender.send(serverAnswer, username));
    }
    public synchronized void clear(String username){
        try {
            movieList = databaseCommunicator.clear(username);
            serverAnswer = new ServerAnswer("All your elements have been deleted from collection.");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        } catch (ClassNotFoundException e) {
            serverAnswer = new ServerAnswer("Something went wrong. Check database drivers.");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        } catch (SQLException e) {
            serverAnswer = new ServerAnswer("Database is not available now.");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
    }
    public void help(String username){
        serverAnswer = new ServerAnswer("Commands :\n"+
                "info : collection information.\n"+
                "show : all elements of collection.\n"+
                "add {element} : add new element in collection.\n"+
                "update id {element} : update element value by its id.\n"+
                "remove_by_id id : delete element from collection by its id.\n"+
                "clear : delete all elements from collection.\n"+
                "save : save collection to the file.\n"+
                "execute_script file_name : read and execute script from the file.\n"+
                "exit : exit without saving collection to the file.\n"+
                "insert_at index {element} : add new element to the specified index.\n"+
                "remove_greater {element} : remove all elements that exceed the specified.\n"+
                "history : show last 8 commands without their arguments.\n"+
                "group_counting_by_id : group collection elements by their id and show count of elements in every group.\n"+
                "filter_less_than_mpaa_rating mpaaRating : show elements with mpaa rating lower than specified.\n"+
                "print_descending : show elements in descending order.");
        poolSender.execute(() -> serverSender.send(serverAnswer, username));
    }
    public void save(){
        CollectionSaver saver = new CollectionSaver(this);
        saver.save();
    }
    public synchronized void insertAt(int index, Movie movie, String username){
        if (index < 0 || index > movieList.size()){
            serverAnswer = new ServerAnswer("Index should be from 0 to "+movieList.size()+".");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
        else {
            try {
                databaseCommunicator.remove4Insert(index);
                databaseCommunicator.add(movie, username);
                databaseCommunicator.add4Insert();
                movieList.clear();
                databaseCommunicator.load(movieList);
                serverAnswer = new ServerAnswer("Element inserted");
                poolSender.execute(() -> serverSender.send(serverAnswer, username));
            } catch (ClassNotFoundException e) {
                serverAnswer = new ServerAnswer("Something went wrong. Check database drivers.");
                poolSender.execute(() -> serverSender.send(serverAnswer, username));
            } catch (SQLException e) {
                serverAnswer = new ServerAnswer("Database is not available now.");
                poolSender.execute(() -> serverSender.send(serverAnswer, username));
            }
        }
    }
    public synchronized void removeGreater(Movie movie, String username){
        try {
            movieList = databaseCommunicator.removeGreater(movie, username);
            serverAnswer = new ServerAnswer("All your elements with oscars count higher that was inserted were deleted.");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        } catch (ClassNotFoundException e) {
            serverAnswer = new ServerAnswer("Something went wrong. Check database drivers.");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        } catch (SQLException e) {
            serverAnswer = new ServerAnswer("Database is not available now.");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
    }
    public void mpaaRatingFilter(MpaaRating mpaaRating, String username){
        MpaaRatingComparator comparator = new MpaaRatingComparator();
        int sch=0;
        String ans = "";
        for (Movie i:movieList){
            if (comparator.getIntValue(i.getMpaaRating())<comparator.getIntValue(mpaaRating)){
                ans = ans+i+"\n";
                sch+=1;
            }
        }
        if (sch==0){
            serverAnswer = new ServerAnswer("No elements with mpaa rating lower than introduced.");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
        else{
            serverAnswer = new ServerAnswer(ans);
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
    }
    public void printDescending(String username){
        String ans="";
        if (!movieList.isEmpty()) {
            for (int i = movieList.size() - 1; i > -1; i -= 1) {
                ans = ans + movieList.get(i) + "\n";
            }
            serverAnswer = new ServerAnswer(ans);
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
        else{
            serverAnswer = new ServerAnswer("There is no any elements in collection.");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
    }
    public void exit(){
        ConsoleReader.setRunnerFlag(false);
    }
    public void history(String username){
        if (HistorySafe.getHistory().isEmpty()){
            serverAnswer = new ServerAnswer("You have not entered commands yet");
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
        else{
            String outPut="Last commands (from 1 to 8):\n";
            for (String i:HistorySafe.getHistory()){
                outPut = outPut+i+" ";
            }
            serverAnswer = new ServerAnswer(outPut);
            poolSender.execute(() -> serverSender.send(serverAnswer, username));
        }
    }
    public void groupCountingId(String username){
        List<Movie> d2 = new ArrayList<>();
        List<Movie> ud2 = new ArrayList<>();
        for (Movie i:movieList){
            if (i.getID()%2==0){d2.add(i);}
            else{ud2.add(i);}
        }
        serverAnswer = new ServerAnswer("2 groups were created.\n" +
                "Count of group 1 (elements with ID which is divisible by 2):"+d2.size()+"\n" +
                "Count of group 2 (elements with ID which is not divisible by 2):"+ud2.size());
        poolSender.execute(() -> serverSender.send(serverAnswer, username));
    }

    public void login(String user, String password){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            String hashedPassword = new String(messageDigest.digest(password.getBytes()));
            if (databaseCommunicator.logIn(user, hashedPassword)){
                serverAnswer = new ServerAnswer("You are authorized. Write sign_out to exit from account.");
            }
            else {
                serverAnswer = new ServerAnswer("Invalid login or password.");
            }
            poolSender.execute(() -> serverSender.send(serverAnswer, user));
        } catch (ClassNotFoundException e) {
            serverAnswer = new ServerAnswer("Something went wrong. Check database drivers.");
            poolSender.execute(() -> serverSender.send(serverAnswer, user));
        } catch (SQLException e) {
            serverAnswer = new ServerAnswer("Database is not available now.");
            poolSender.execute(() -> serverSender.send(serverAnswer, user));
        } catch (NoSuchAlgorithmException e) {
            serverAnswer = new ServerAnswer("Can not hash your password. Try again.");
            poolSender.execute(() -> serverSender.send(serverAnswer, user));
        }
    }

    public void register(String user, String password){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            String hashedPassword =  new String(messageDigest.digest(password.getBytes()));
            if (databaseCommunicator.register(user, hashedPassword)){
                serverAnswer = new ServerAnswer("Account created. Now you have to log in.");
            }
            else {
                serverAnswer = new ServerAnswer("This username is already reserved. Try another");
            }
            poolSender.execute(() -> serverSender.send(serverAnswer, user));
        } catch (ClassNotFoundException e) {
            serverAnswer = new ServerAnswer("Something went wrong. Check database drivers.");
            poolSender.execute(() -> serverSender.send(serverAnswer, user));
        } catch (SQLException e) {
            serverAnswer = new ServerAnswer("Database is not available now.");
            poolSender.execute(() -> serverSender.send(serverAnswer, user));
        } catch (NoSuchAlgorithmException e) {
            serverAnswer = new ServerAnswer("Can not hash your password. Try again.");
            poolSender.execute(() -> serverSender.send(serverAnswer, user));
        }
    }

    public void executeScript(String fileName) {
        FilesSaver filesSaver = new FilesSaver();
        String[] arrayOfParams;
        FileReader reader = new FileReader();
        FileChecker fileChecker = new FileChecker();
        if (fileChecker.check(fileName)) {
            reader.read(fileName);
            filesSaver.save(fileName);
            Invoker invoker = new Invoker();
            for (String i : reader.getCommandsList()) {
                String[] params={""};
                arrayOfParams = i.split(" +");
                if (arrayOfParams.length > 1) {
                    params = new String[arrayOfParams.length - 1];
                    System.arraycopy(arrayOfParams, 1, params, 0, arrayOfParams.length - 1);
                    try{
                        invoker.findCommand(arrayOfParams[0], params[0]);
                        HistorySaver historySaver = new HistorySaver();
                        historySaver.save(arrayOfParams[0]);
                    }
                    catch (InvalidNameException | NonArgsExeption | TooManyArgsException e){
                        System.out.println(e.getMessage());
                    }
                }
                if (params.length <= 1) {
                    try {
                        invoker.findCommand(arrayOfParams[0], params[0]);
                        HistorySaver historySaver = new HistorySaver();
                        historySaver.save(arrayOfParams[0]);
                    } catch (InvalidNameException | NonArgsExeption | TooManyArgsException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("Too many parameters. Use \"help\" to get a list of available commands.");
                }
            }
        }
        else{
            System.out.println("File recursion found.");
        }

        FilesSafe.setFileNamesList(new ArrayList<String>());
    }
}

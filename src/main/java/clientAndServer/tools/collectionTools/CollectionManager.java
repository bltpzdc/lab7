package clientAndServer.tools.collectionTools;

import clientAndServer.startingData.CustomVector;
import clientAndServer.startingData.Movie;
import clientAndServer.startingData.MpaaRating;
import clientAndServer.tools.consoleTools.ConsoleReader;
import clientAndServer.tools.insideCommands.*;
import server.tools.ServerAnswer;
import server.tools.ServerSender;
import clientAndServer.tools.insideCommands.*;
import clientAndServer.tools.worksWithCommands.Invoker;
import clientAndServer.exeptions.InvalidNameException;
import clientAndServer.exeptions.NonArgsExeption;
import clientAndServer.exeptions.TooManyArgsException;
import lombok.Getter;
import clientAndServer.startingData.*;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionManager {
    @Getter
    private CustomVector<Movie> movieList;
    private UserInput userInput = new UserInput();
    private CollectionLoader loader;

    public CollectionManager(CollectionLoader loader){
        this.loader=loader;
        movieList=loader.getMovieList();
    }

    public void info(){
        String[] listik =  movieList.getClass().getName().split("\\.");
        ServerAnswer serverAnswer = new ServerAnswer( "Collection information:\n"+
                "Type: "+listik[listik.length-1]+".\n"+
                "Initialize date: "+movieList.getInitTime()+".\n"+
                "Element count: "+movieList.size());
        ServerSender serverSender = new ServerSender();
        serverSender.send(serverAnswer);
    }

    public void show(){
        String ans="";
        if (movieList.size()==0){
            ServerAnswer serverAnswer = new ServerAnswer("There's no any elements in collection.");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
        else{
            for (Movie i:movieList){
                ans = ans+i+"\n";
            }
            ServerAnswer serverAnswer = new ServerAnswer(ans);
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
    }

    public void add(Movie movie){
        int id;
        ArrayList<Integer> idList = new ArrayList<>();
        if (movieList.size()>0) {
            for (Movie i : movieList) {
                idList.add(i.getID());
            }
            id = Collections.max(idList) + 1;
        }
        else {id=1;}
        movie.setId(id);
        movieList.add(movie);
        ServerAnswer serverAnswer = new ServerAnswer("New element has been added to collection.");
        ServerSender serverSender = new ServerSender();
        serverSender.send(serverAnswer);
    }

    public void update(int id, Movie movie){
        int needUpdate=0;
        for (Movie sch:movieList){
            if (sch.getID()==id) {needUpdate+=1;}
        }
        if ((needUpdate==0)&&(id>=0)){
            ServerAnswer serverAnswer = new ServerAnswer("There is no any elements with this ID.");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
        if (needUpdate>0) {
            movieList.removeIf(i -> i.getID() == id);
            movie.setId(id);
            movieList.add(movie);
            Collections.sort(movieList);
            ServerAnswer serverAnswer = new ServerAnswer("Element has been updated.");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
    }

    public void remove(int id){
        int needRemove=0;
        for (Movie mo:movieList){
            if (mo.getID()==id){needRemove+=1;}
        }
        if ((needRemove==0)&&(id>=0)){
            ServerAnswer serverAnswer = new ServerAnswer("There is no any elements with this ID.");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
        else if (id<0){
            ServerAnswer serverAnswer = new ServerAnswer("ID should be higher than 0.");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
        if (needRemove>0) {
            //movieList = (CustomVector<Movie>) movieList.stream().filter(i -> i.getID()!=id).collect(Collectors.toList());
            movieList.removeIf(i -> i.getID() == id);
            ServerAnswer serverAnswer = new ServerAnswer("Element has been deleted from collection.");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
    }
    public void clear(){
        movieList.clear();
        ServerAnswer serverAnswer = new ServerAnswer("All elements have been deleted from collection.");
        ServerSender serverSender = new ServerSender();
        serverSender.send(serverAnswer);
    }
    public void help(){
        ServerAnswer serverAnswer = new ServerAnswer("Commands :\n"+
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
        ServerSender serverSender = new ServerSender();
        serverSender.send(serverAnswer);
    }
    public void save(){
        CollectionSaver saver = new CollectionSaver(this);
        saver.save();
    }
    public void insertAt(int index, Movie movie){
        if ((index>-1)&&(index<=movieList.size())) {
            int id;
            ArrayList<Integer> idList = new ArrayList<>();
            if (movieList.size() > 0) {
                for (Movie i : movieList) {
                    idList.add(i.getID());
                }
                id = Collections.max(idList) + 1;
            } else {
                id = 1;
            }
            movie.setId(id);
            movieList.add(index, movie);
            ServerAnswer serverAnswer = new ServerAnswer("Element has been added");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
        else if (index>0){
            ServerAnswer serverAnswer = new ServerAnswer("Index is bigger than you can enter. Please, enter index from 0 to "+(movieList.size())+".");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
        else if (index<1){
            ServerAnswer serverAnswer = new ServerAnswer("Index can not be lower than 0. Please, enter index from 0 to "+(movieList.size()-1)+".");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
    }
    public void removeGreater(Movie movie){
        ArrayList<Movie> movs =(ArrayList<Movie>) movieList.stream().filter(i -> i.getID() >= movie.getID()).collect(Collectors.toList());
        movieList.clear();
        movieList.addAll(movs);
        ServerAnswer serverAnswer = new ServerAnswer("Elements have been deleted.");
        ServerSender serverSender = new ServerSender();
        serverSender.send(serverAnswer);
    }
    public void mpaaRatingFilter(MpaaRating mpaaRating){
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
            ServerAnswer serverAnswer = new ServerAnswer("No elements with mpaa rating lower than introduced.");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
        else{
            ServerAnswer serverAnswer = new ServerAnswer(ans);
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
    }
    public void printDescending(){
        String ans="";
        if (!movieList.isEmpty()) {
            for (int i = movieList.size() - 1; i > -1; i -= 1) {
                ans = ans + movieList.get(i) + "\n";
            }
            ServerAnswer serverAnswer = new ServerAnswer(ans);
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
        else{
            ServerAnswer serverAnswer = new ServerAnswer("There is no any elements in collection.");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
    }
    public void exit(){
        ConsoleReader.setRunnerFlag(false);
    }
    public void history(){
        if (HistorySafe.getHistory().isEmpty()){
            ServerAnswer serverAnswer = new ServerAnswer("You have not entered commands yet");
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
        else{
            String outPut="Last commands (from 1 to 8):\n";
            for (String i:HistorySafe.getHistory()){
                outPut = outPut+i+" ";
            }
            ServerAnswer serverAnswer = new ServerAnswer(outPut);
            ServerSender serverSender = new ServerSender();
            serverSender.send(serverAnswer);
        }
    }
    public void groupCountingId(){
        List<Movie> d2 = new ArrayList<>();
        List<Movie> ud2 = new ArrayList<>();
        for (Movie i:movieList){
            if (i.getID()%2==0){d2.add(i);}
            else{ud2.add(i);}
        }
        ServerAnswer serverAnswer = new ServerAnswer("2 groups were created.\n" +
                "Count of group 1 (elements with ID which is divisible by 2):"+d2.size()+"\n" +
                "Count of group 2 (elements with ID which is not divisible by 2):"+ud2.size());
        ServerSender serverSender = new ServerSender();
        serverSender.send(serverAnswer);
    }
    public void execScript(){

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

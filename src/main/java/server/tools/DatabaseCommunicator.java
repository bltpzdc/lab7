package server.tools;

import clientAndServer.startingData.*;
import clientAndServer.tools.insideCommands.MovieBuilder;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class DatabaseCommunicator {
    private static HashMap<Long, Movie> bufferedMovies;
    private static ArrayList bufferedOwners;
    private Connection connection = null;
    private String request;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private static String DBLink;
    private static String USERNAME;
    private static String PASSWORD;

    public void lazyInitConnection() throws SQLException {
        if (connection == null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("DB_info.txt")))) {
                /*BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("DB_info.txt")));*/
                DBLink = reader.readLine();
                USERNAME = reader.readLine();
                PASSWORD = reader.readLine();
            } catch (IOException ignored){}
            connection = DriverManager.getConnection(DBLink, USERNAME, PASSWORD);
        }
    }

    public boolean canFindMovieByID(int id) throws ClassNotFoundException, SQLException {
        lazyInitConnection();
        Class.forName("org.postgresql.Driver");
        request = "select * from \"Movies\" where id = ?";
        preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
        resultSet = preparedStatement.getResultSet();
        return resultSet.next();
    }

    public boolean deleteMovie(int id, String username) throws SQLException, ClassNotFoundException {
        lazyInitConnection();
        Class.forName("org.postgresql.Driver");
        request = "select * from \"Movies\" where id = ? and owner = ?";
        preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, username);
        preparedStatement.execute();
        resultSet = preparedStatement.getResultSet();
        if (resultSet.next()){
            request = "select person from \"Movies\" where owner = ?";
            preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, username);
            preparedStatement.execute();
            int personID=0;
            if (preparedStatement.getResultSet().next()) {
                personID = preparedStatement.getResultSet().getInt(1);
            }
            request = "delete from \"Movies\" where id = ?";
            preparedStatement = connection.prepareStatement(request);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            String personRequest = "delete from \"Persons\" where id = ?";
            preparedStatement = connection.prepareStatement(personRequest);
            preparedStatement.setInt(1, personID);
            preparedStatement.execute();
            return true;
        }
        else return false;
    }

    public void add(Movie movie, String username) throws SQLException, ClassNotFoundException {
        lazyInitConnection();
        Class.forName("org.postgresql.Driver");
        Person addPerson = movie.getDirector();
        request = "insert into \"Persons\" (name, birthday, eye_color, hair_color, nationality) values (?, ?, ?, ?, ?) returning id";
        preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, addPerson.getName());
        preparedStatement.setDate(2, new java.sql.Date(addPerson.getBirthday().getTime()));
        preparedStatement.setInt(3, addPerson.getEyeColor().ordinal());
        preparedStatement.setInt(4, addPerson.getHairColor().ordinal());
        preparedStatement.setInt(5, addPerson.getNationality().ordinal());
        preparedStatement.execute();
        resultSet = preparedStatement.getResultSet();
        if (resultSet.next()) {
            int personID = resultSet.getInt(1);
            String addingMovie = "insert into \"Movies\" (name, x, y, creation_date, oscars_count, genre, mpaa_rating, person, owner) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(addingMovie);
            preparedStatement.setString(1, movie.getName());
            preparedStatement.setDouble(2, movie.getCoordinates().getX());
            preparedStatement.setFloat(3, movie.getCoordinates().getY());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(movie.getCreationDate().atStartOfDay()));
            preparedStatement.setLong(5, movie.getOscarsCount());
            preparedStatement.setInt(6, movie.getGenre().ordinal());
            preparedStatement.setInt(7, movie.getMpaaRating().ordinal());
            preparedStatement.setInt(8, personID);
            preparedStatement.setString(9, username);
            preparedStatement.execute();
        }
    }

    public CustomVector<Movie> removeGreater(Movie movie, String username) throws SQLException, ClassNotFoundException {
        request = "select person from \"Movies\" where oscars_count > ? and owner = ?";
        preparedStatement = connection.prepareStatement(request);
        Long oscarsCount = movie.getOscarsCount();
        preparedStatement.setLong(1, oscarsCount);
        preparedStatement.setString(2, username);
        preparedStatement.execute();
        resultSet = preparedStatement.getResultSet();
        while (resultSet.next()){
            request = "delete from \"Movies\" where oscars_count > ? and owner = ?";
            preparedStatement = connection.prepareStatement(request);
            preparedStatement.setLong(1, movie.getOscarsCount());
            preparedStatement.setString(2, username);
            preparedStatement.execute();
            String delPerson = "delete from \"Persons\" where id = ?";
            preparedStatement = connection.prepareStatement(delPerson);
            preparedStatement.setInt(1, resultSet.getInt(1));
            preparedStatement.execute();
        }
        CustomVector<Movie> collection = new CustomVector<>();
        load(collection);
        return collection;
    }

    public boolean update(int id, Movie movie, String username) throws SQLException {
        String request = "select * from \"Movies\" where id = ? and owner = ?";
        preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, username);
        preparedStatement.execute();
        resultSet = preparedStatement.getResultSet();
        if (resultSet.next()){
            long perID = resultSet.getLong(9);
            String updMovies = "update \"Movies\" set name = ?, x = ?, y = ?, creation_date = ?, oscars_count = ?, genre = ?, mpaa_rating = ? where id = ?";
            preparedStatement = connection.prepareStatement(updMovies);
            preparedStatement.setString(1, movie.getName());
            preparedStatement.setDouble(2, movie.getCoordinates().getX());
            preparedStatement.setFloat(3, movie.getCoordinates().getY());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(movie.getCreationDate().atStartOfDay()));
            preparedStatement.setLong(5, movie.getOscarsCount());
            preparedStatement.setInt(6, movie.getGenre().ordinal());
            preparedStatement.setInt(7, movie.getMpaaRating().ordinal());
            preparedStatement.setInt(8, id);
            preparedStatement.execute();
            String updPersons = "update \"Persons\" set name = ?, birthday = ?, eye_color = ?, hair_color = ?, nationality = ? where id = ?";
            Person addPerson = movie.getDirector();
            preparedStatement = connection.prepareStatement(updPersons);
            preparedStatement.setString(1, addPerson.getName());
            preparedStatement.setDate(2, new java.sql.Date(addPerson.getBirthday().getTime()));
            preparedStatement.setInt(3, addPerson.getEyeColor().ordinal());
            preparedStatement.setInt(4, addPerson.getHairColor().ordinal());
            preparedStatement.setInt(5, addPerson.getNationality().ordinal());
            preparedStatement.setInt(6, (int) perID);
            preparedStatement.execute();
            return true;
        }
        return false;
    }
    public CustomVector<Movie> clear(String username) throws SQLException, ClassNotFoundException {
        lazyInitConnection();
        Class.forName("org.postgresql.Driver");
        request = "select person from \"Movies\" where owner = ?";
        preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, username);
        preparedStatement.execute();
        resultSet = preparedStatement.getResultSet();
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            request = "delete from \"Movies\" where owner = ?";
            preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, username);
            preparedStatement.execute();
            String personRequest = "delete from \"Persons\" where id = ?";
            preparedStatement = connection.prepareStatement(personRequest);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        }
        CustomVector<Movie> collection = new CustomVector<>();
        load(collection);
        return collection;
    }

    public boolean logIn(String username, String password) throws SQLException, ClassNotFoundException {
        lazyInitConnection();
        Class.forName("org.postgresql.Driver");
        request = "select * from \"user_data\" where name = ? and password = ?";
        preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.execute();
        resultSet = preparedStatement.getResultSet();
        return resultSet.next();
    }

    public boolean register(String username, String password) throws SQLException, ClassNotFoundException {
        lazyInitConnection();
        Class.forName("org.postgresql.Driver");
        request = "select * from \"user_data\" where name = ?";
        preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, username);
        preparedStatement.execute();
        resultSet = preparedStatement.getResultSet();
        if (!resultSet.next()){
            request = "insert into \"user_data\" values (?, ?)";
            preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.execute();
            return true;
        }
        else return false;
    }

    public void remove4Insert(int index) throws SQLException {
        lazyInitConnection();
        request = "select M.id, M.name, x, y, creation_date, oscars_count, genre, mpaa_rating, owner, P.name, birthday, eye_color, hair_color, nationality, P.id from \"Movies\" as M inner join \"Persons\" P on M.person = P.id";
        preparedStatement = connection.prepareStatement(request);
        preparedStatement.execute();
        resultSet = preparedStatement.getResultSet();
        int counter = 0;
        HashMap<Long, Movie> bufferList = new HashMap<>();
        List<String> personsID = new ArrayList<>();
        var movieBuilder = Movie.builder();
        while (resultSet.next()){
            counter++;
            if (counter > index){
                movieBuilder.id(resultSet.getInt(1));
                movieBuilder.name(resultSet.getString(2));
                movieBuilder.coordinates(new Coordinates(resultSet.getDouble(3), resultSet.getFloat(4)));
                movieBuilder.creationDate((LocalDate) resultSet.getObject(5, LocalDate.class));
                movieBuilder.oscarsCount(resultSet.getLong(6));
                movieBuilder.genre(MovieGenre.values()[resultSet.getInt(7)]);
                movieBuilder.mpaaRating(MpaaRating.values()[resultSet.getInt(8)]);
                String owner = resultSet.getString(9);
                movieBuilder.director(new Person(resultSet.getString(10), resultSet.getDate(11), EyeColor.values()[resultSet.getInt(12)], HairColor.values()[resultSet.getInt(13)], Country.values()[resultSet.getInt(14)]));
                long perID = resultSet.getLong(15);
                personsID.add(owner);
                bufferList.put(perID, movieBuilder.build());
                String rmRequest = "delete from \"Movies\" where id = ?";
                preparedStatement = connection.prepareStatement(rmRequest);
                preparedStatement.setInt(1, resultSet.getInt(1));
                preparedStatement.execute();
                rmRequest = "delete from \"Persons\" where id = ?";
                preparedStatement = connection.prepareStatement(rmRequest);
                preparedStatement.setLong(1, perID);
                preparedStatement.execute();
            }
        }
        bufferedMovies = bufferList;
        bufferedOwners = (ArrayList) personsID;
    }
    public void add4Insert() throws SQLException {
        Set<Long> keys = bufferedMovies.keySet();
        int counter = 0;
        for (long i:keys){
            Movie movie = bufferedMovies.get(i);
            String owner = (String) bufferedOwners.get(counter++);
            String addPerson = "insert into \"Persons\" (id, name, birthday, eye_color, hair_color, nationality) values (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(addPerson);
            preparedStatement.setLong(1, i);
            preparedStatement.setString(2, movie.getDirector().getName());
            preparedStatement.setDate(3, new java.sql.Date(movie.getDirector().getBirthday().getTime()));
            preparedStatement.setInt(4, movie.getDirector().getEyeColor().ordinal());
            preparedStatement.setInt(5, movie.getDirector().getHairColor().ordinal());
            preparedStatement.setInt(6, movie.getDirector().getNationality().ordinal());
            preparedStatement.execute();
            String addMovie = "insert into \"Movies\" (id, name, x, y, creation_date, oscars_count, genre, mpaa_rating, person, owner) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(addMovie);
            preparedStatement.setInt(1, movie.getID());
            preparedStatement.setString(2, movie.getName());
            preparedStatement.setDouble(3, movie.getCoordinates().getX());
            preparedStatement.setFloat(4, movie.getCoordinates().getY());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(movie.getCreationDate().atStartOfDay()));
            preparedStatement.setLong(6, movie.getOscarsCount());
            preparedStatement.setInt(7, movie.getGenre().ordinal());
            preparedStatement.setInt(8, movie.getMpaaRating().ordinal());
            preparedStatement.setInt(9, (int) i);
            preparedStatement.setString(10, owner);
            preparedStatement.execute();
        }
    }
    public void load(CustomVector<Movie> movies) throws SQLException, ClassNotFoundException {
        lazyInitConnection();
        Class.forName("org.postgresql.Driver");
        var movieBuilder = Movie.builder();
        request = "select M.id, M.name, x, y, creation_date, oscars_count, genre, mpaa_rating, P.name, birthday, eye_color, hair_color, nationality from \"Movies\" as M inner join \"Persons\" P on M.person = P.id";
        preparedStatement = connection.prepareStatement(request);
        preparedStatement.execute();
        resultSet = preparedStatement.getResultSet();
        while (resultSet.next()){
            movieBuilder.id(resultSet.getInt(1));
            movieBuilder.name(resultSet.getString(2));
            movieBuilder.coordinates(new Coordinates(resultSet.getDouble(3), resultSet.getFloat(4)));
            movieBuilder.creationDate((LocalDate) resultSet.getObject(5, LocalDate.class));
            movieBuilder.oscarsCount(resultSet.getLong(6));
            movieBuilder.genre(MovieGenre.values()[resultSet.getInt(7)]);
            movieBuilder.mpaaRating(MpaaRating.values()[resultSet.getInt(8)]);
            movieBuilder.director(new Person(resultSet.getString(9), resultSet.getDate(10), EyeColor.values()[resultSet.getInt(11)], HairColor.values()[resultSet.getInt(12)], Country.values()[resultSet.getInt(13)]));
            movies.add(movieBuilder.build());
        }
    }
}

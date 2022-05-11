package clientAndServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    static final String DB_URL = "jdbc:postgresql://localhost:63333/studs";
    static final String name = "s336762";
    static final String password = "hbc698";
    //ssh -L 63333:pg:5432 s336762@se.ifmo.ru -p 2222
    public static void main(String[] args){
        Connection connection;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, name, password);
            System.out.println("Подключение успешно!");
        } catch (SQLException e){
            System.out.println("Фигню написал 2.0");
        } catch (ClassNotFoundException e){
            System.out.println("Фигню написал");
        }
    }
}

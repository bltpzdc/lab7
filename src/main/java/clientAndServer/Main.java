package clientAndServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String name = "postgres";
    static final String password = "5NbQhXEe";
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

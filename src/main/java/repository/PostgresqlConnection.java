package repository;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresqlConnection{
    private static Connection connection;

    public static Connection getConnection(){
        if(connection != null)
            return connection;

        try{
            connection = DriverManager.getConnection(
                    Env.DB_URL,
                    Env.DB_USERNAME,
                    Env.DB_PASSWORD
            );
            return connection;
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            throw new RuntimeException("Connection failed");
        }
    }

    public static void closeConnection(){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
package repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SimpleCrud{
    static private final Connection connection = PostgresqlConnection.getConnection();
    public static boolean delete(String tableName, String id){
        String query = Query.delete(tableName);
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.executeUpdate();
            return true;
        }catch (SQLException error){
            System.out.println(error.getMessage());
        }
        return false;
    }
}
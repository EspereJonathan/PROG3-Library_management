package repository;


import model.Visitor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitorCrudOperations implements CrudOperations<Visitor>{
    private static final Connection connection = PostgresqlConnection.getConnection();

    public static Visitor visitorInstance(ResultSet resultSet) throws SQLException {
        return new Visitor(
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getString("sex")
        );
    }

    @Override
    public List<Visitor> findAll() {
        String query =  Query.selectAll("subscriber");
        List<Visitor> visitors = new ArrayList<>();

        try{
            ResultSet resultSet = connection.prepareStatement(query).executeQuery();
            while(resultSet.next()){
                visitors.add(visitorInstance(resultSet));
            }
        } catch (SQLException error){
            System.out.println(error.getMessage());
        }

        return visitors;
    }

    @Override
    public List<Visitor> saveAll(List<Visitor> toSave) {
        List<Visitor> visitors = new ArrayList<>();
        toSave.forEach(el -> visitors.add(save(el)));
        return visitors;
    }

    @Override
    public Visitor save(Visitor toSave) {
        String query = Query.create("visitor", List.of("name", "ref"));
        Visitor visitor = null;

        try{
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,toSave.getName());
            statement.setString(2,toSave.getRef());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                toSave.setId(resultSet.getString(1));
                visitor = toSave;
            }
        }catch (SQLException error){
            System.out.println(error.getMessage());
        }

        return visitor;
    }

    @Override
    public Visitor delete(Visitor toDelete) {
        return SimpleCrud.delete("subscriber", toDelete.getId()) ? toDelete : null;
    }
}
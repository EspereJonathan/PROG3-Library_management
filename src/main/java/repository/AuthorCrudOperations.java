package repository;

import model.Author;
import model.Sex;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorCrudOperations implements CrudOperations<Author>{
    private static final Connection connection = PostgresqlConnection.getConnection();

    public static Author authorInstance(ResultSet resultSet) throws SQLException {
        return new Author(
            resultSet.getString("id"),
            resultSet.getString("name"),
            Sex.valueOf(resultSet.getString("sex"))
        );
    }

    @Override
    public List<Author> findAll() {
        String query =  Query.selectAll("author");
        List<Author> authors = new ArrayList<>();

        try{
            ResultSet resultSet = connection.prepareStatement(query).executeQuery();
            while(resultSet.next()){
                authors.add(authorInstance(resultSet));
            }
        } catch (SQLException error){
            System.out.println(error.getMessage());
        }

        return authors;
    }

    @Override
    public List<Author> saveAll(List<Author> toSave) {
        List<Author> authors = new ArrayList<>();
        toSave.forEach(el -> authors.add(save(el)));
        return authors;
    }

    @Override
    public Author save(Author toSave) {
        String query = Query.create("author", List.of("name", "sex"));
        Author author = null;

        try{
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,toSave.getName());
            statement.setString(2,toSave.getSex().toString());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                toSave.setId(resultSet.getString(1));
                author = toSave;
            }
        }catch (SQLException error){
            System.out.println(error.getMessage());
        }

        return author;
    }

    @Override
    public Author delete(Author toDelete) {
        return BasicCrud.delete("author", toDelete.getId()) ? toDelete : null;
    }

    public static Author getOne(String id){
        String query = "SELECT * FROM \"author\" WHERE id = ? ;";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return authorInstance(resultSet);
            }
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
        }
        return null;
    }
}

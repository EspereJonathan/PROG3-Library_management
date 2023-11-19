package repository;


import model.Book;
import model.Topic;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookCrudOperations implements CrudOperations<Book> {
    private final Connection connection = PostgresqlConnection.getConnection();
    public static Book bookInstance(ResultSet resultSet) throws SQLException {
        List<Topic> topics = Arrays.stream((String[]) resultSet.getArray("topics")
                .getArray()).map(Topic::valueOf).toList();
        return new Book(
            resultSet.getString("id"),
            resultSet.getString("book_name"),
            resultSet.getLong("page_numbers"),
            resultSet.getDate("release_date"),
            topics,
            AuthorCrudOperations.getOne(resultSet.getString("id_author"))
        );
    }

    @Override
    public List<Book> findAll() {
        String query =  Query.selectAll("book");
        List<Book> books = new ArrayList<>();

        try{
            ResultSet resultSet = connection.prepareStatement(query).executeQuery();
            while(resultSet.next()){
                books.add(bookInstance(resultSet));
            }
        } catch (SQLException error){
            System.out.println(error.getMessage());
        }
        return books;
    }

    @Override
    public List<Book> saveAll(List<Book> toSave) {
        List<Book> books = new ArrayList<>();
        toSave.forEach(el -> books.add(save(el)));
        return books;
    }

    @Override
    public Book save(Book toSave) {
        String topics = toSave.getTopics().stream()
                .map(el -> "'" + el.toString() + "'::\"topic\"")
                .collect(Collectors.joining(", "));
        String query =
            "INSERT INTO \"book\"(\"book_name\", \"page_numbers\", \"release_date\", \"topics\") " +
            "VALUES (?, ? , ?, ARRAY[" + topics + "]);";
        Book book = null;

        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, toSave.getBookName());
            statement.setLong(2, toSave.getPageNumbers());
            statement.setDate(3, toSave.getReleaseDate());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                toSave.setId(resultSet.getString(1));
                book = toSave;
            }
        }catch (SQLException error){
            System.out.println(error.getMessage());
        }
        return book;
    }

    @Override
    public Book delete(Book toDelete) {
        return SimpleCrud.delete("book", toDelete.getId()) ? toDelete : null;
    }
}
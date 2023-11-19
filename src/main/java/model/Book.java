package model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {
    private int bookId;
    private String bookName;
    private int pageNumbers;
    private String topic;
    private LocalDate releaseDate;
    private String status;
    private Author author;

    public Book(int bookId, String bookName, int pageNumbers, String topic, LocalDate releaseDate, String status, Author author) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.pageNumbers = pageNumbers;
        this.topic = topic;
        this.releaseDate = releaseDate;
        this.status = status;
        this.author = author;
    }

}

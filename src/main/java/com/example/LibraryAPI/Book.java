package com.example.LibraryAPI;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Entity
@Data
@Table(name = "books")
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long isbn;

    @Column(unique = true)
    private String title;

    private String genre;

    private String description;

    private String author;

    public Book(Long isbn, String title, String description, String genre, String author){
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.author = author;
    }

    public Book(){}


    @Override
    public String toString(){
        return "Book{" +
                "id= " + id +
                ", isbn= " + isbn +
                ", title= " + title +
                ", description= " + description +
                ", genre= " + genre +
                ", author= " + author +
                '}';
    }


}

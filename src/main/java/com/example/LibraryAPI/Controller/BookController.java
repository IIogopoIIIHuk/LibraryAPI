package com.example.LibraryAPI.Controller;


import com.example.LibraryAPI.Book;
import com.example.LibraryAPI.Repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/api/add")
    public void addBook(@RequestBody Book book){
        bookRepository.save(book);
    }

    @GetMapping("/api/all")
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }


    @GetMapping("/api/bookByIsbn")
    public Book getBookByIsbn(@RequestParam Long isbn){
        return bookRepository.findByIsbn(isbn).orElseThrow();
    }

    @GetMapping("/api/specialBook")
    public Book getSpecialBook(@RequestParam Long id){
        return bookRepository.findById(id).orElseThrow();
    }

    @PutMapping("/api/update")
    public String updateBook(@RequestBody Book book){
        if(!bookRepository.existsById(book.getId())) {
            return "not found book";
        }
        return bookRepository.save(book).toString();
    }

    @DeleteMapping("/api/delete")
    public void deleteBook(@RequestParam Long id){
        bookRepository.deleteById(id);
    }

}

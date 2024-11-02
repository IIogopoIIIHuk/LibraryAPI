package com.example.LibraryAPI.Controller;


import com.example.LibraryAPI.Book;
import com.example.LibraryAPI.Repo.BookRepository;
import com.example.LibraryAPI.Service.LibraryService;
import jdk.jfr.Percentage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final LibraryService libraryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void addBook(@RequestBody Book book){
        bookRepository.save(book);
        libraryService.notifyNewBook(book.getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }


    @GetMapping("/isbn")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Book getBookByIsbn(@RequestParam Long isbn){
        return bookRepository.findByIsbn(isbn).orElseThrow();
    }

    @GetMapping("/byId")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Book getBookById(@RequestParam Long id){
        return bookRepository.findById(id).orElseThrow();
    }

    @PutMapping("/id-update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateBook(@RequestParam Long id, @RequestBody Book book){
        if(!bookRepository.existsById(book.getId())) {
            return "not found book";
        }
        return bookRepository.save(book).toString();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook(@PathVariable Long id){
        bookRepository.deleteById(id);
    }

}

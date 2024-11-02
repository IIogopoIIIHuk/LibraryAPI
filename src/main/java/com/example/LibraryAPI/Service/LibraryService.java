package com.example.LibraryAPI.Service;

import com.example.LibraryAPI.AvailableBook;
import com.example.LibraryAPI.Repo.AvailableBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final AvailableBookRepository availableBookRepository;


    public void addAvailableBook(Long bookId, LocalDateTime checkoutTime, LocalDateTime returnTime) {
        AvailableBook availableBook = new AvailableBook(bookId, checkoutTime, returnTime);
        availableBookRepository.save(availableBook);
    }

    public void notifyNewBook(Long bookId) {
        AvailableBook availableBook = new AvailableBook(bookId, LocalDateTime.now(), null);
        availableBookRepository.save(availableBook);
    }

    public List<AvailableBook> getAvailableBooks() {
        return availableBookRepository.findAll();
    }

    public void updateReturnTime(Long bookId, LocalDateTime returnTime) {
        AvailableBook book = availableBookRepository.findById(bookId).orElseThrow();
        book.setReturnTime(returnTime);
        availableBookRepository.save(book);
    }}

package com.example.LibraryAPI.Service;

import com.example.LibraryAPI.AvailableBook;
import com.example.LibraryAPI.Repo.AvailableBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LibraryServiceTest {

    @Mock
    private AvailableBookRepository availableBookRepository;

    @InjectMocks
    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAvailableBook() {
        // Arrange
        Long bookId = 1L;
        LocalDateTime checkoutTime = LocalDateTime.now();
        LocalDateTime returnTime = checkoutTime.plusDays(7);

        // Act
        libraryService.addAvailableBook(bookId, checkoutTime, returnTime);

        // Assert
        verify(availableBookRepository, times(1)).save(any(AvailableBook.class));
    }

    @Test
    void testNotifyNewBook() {
        // Arrange
        Long bookId = 2L;

        // Act
        libraryService.notifyNewBook(bookId);

        // Assert
        verify(availableBookRepository, times(1)).save(any(AvailableBook.class));
    }

    @Test
    void testGetAvailableBooks() {
        // Arrange
        AvailableBook book1 = new AvailableBook(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        AvailableBook book2 = new AvailableBook(2L, LocalDateTime.now(), LocalDateTime.now().plusDays(5));
        when(availableBookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        // Act
        List<AvailableBook> availableBooks = libraryService.getAvailableBooks();

        // Assert
        assertEquals(2, availableBooks.size());
        assertEquals(book1, availableBooks.get(0));
        assertEquals(book2, availableBooks.get(1));
        verify(availableBookRepository, times(1)).findAll();
    }

    @Test
    void testUpdateReturnTime() {
        // Arrange
        Long bookId = 1L;
        LocalDateTime newReturnTime = LocalDateTime.now().plusDays(10);
        AvailableBook book = new AvailableBook(bookId, LocalDateTime.now(), LocalDateTime.now().plusDays(5));
        when(availableBookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        libraryService.updateReturnTime(bookId, newReturnTime);

        // Assert
        assertEquals(newReturnTime, book.getReturnTime());
        verify(availableBookRepository, times(1)).findById(bookId);
        verify(availableBookRepository, times(1)).save(book);
    }

    @Test
    void testUpdateReturnTime_BookNotFound() {
        // Arrange
        Long bookId = 3L;
        LocalDateTime returnTime = LocalDateTime.now().plusDays(7);
        when(availableBookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> libraryService.updateReturnTime(bookId, returnTime));
        verify(availableBookRepository, times(1)).findById(bookId);
        verify(availableBookRepository, never()).save(any(AvailableBook.class));
    }
}

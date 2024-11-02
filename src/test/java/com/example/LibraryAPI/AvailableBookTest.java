package com.example.LibraryAPI;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AvailableBookTest {

    @Test
    void testAvailableBookCreation() {
        LocalDateTime checkoutTime = LocalDateTime.now();
        LocalDateTime returnTime = checkoutTime.plusDays(7);
        Long bookId = 1L;

        AvailableBook availableBook = new AvailableBook(bookId, checkoutTime, returnTime);

        assertThat(availableBook).isNotNull();
        assertThat(availableBook.getBookId()).isEqualTo(bookId);
        assertThat(availableBook.getCheckoutTime()).isEqualTo(checkoutTime);
        assertThat(availableBook.getReturnTime()).isEqualTo(returnTime);
    }

    @Test
    void testAvailableBookGettersAndSetters() {
        AvailableBook availableBook = new AvailableBook();

        Long bookId = 2L;
        LocalDateTime checkoutTime = LocalDateTime.now();
        LocalDateTime returnTime = checkoutTime.plusDays(10);

        availableBook.setBookId(bookId);
        availableBook.setCheckoutTime(checkoutTime);
        availableBook.setReturnTime(returnTime);

        assertThat(availableBook.getBookId()).isEqualTo(bookId);
        assertThat(availableBook.getCheckoutTime()).isEqualTo(checkoutTime);
        assertThat(availableBook.getReturnTime()).isEqualTo(returnTime);
    }

    @Test
    void testAvailableBookDefaultConstructor() {
        AvailableBook availableBook = new AvailableBook();

        assertThat(availableBook).isNotNull();
        assertThat(availableBook.getBookId()).isNull();
        assertThat(availableBook.getCheckoutTime()).isNull();
        assertThat(availableBook.getReturnTime()).isNull();
    }

    @Test
    void testAvailableBookAllArgsConstructor() {
        LocalDateTime checkoutTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime returnTime = LocalDateTime.of(2024, 1, 8, 10, 0);
        Long bookId = 3L;

        AvailableBook availableBook = new AvailableBook(bookId, checkoutTime, returnTime);

        assertThat(availableBook).isNotNull();
        assertThat(availableBook.getBookId()).isEqualTo(bookId);
        assertThat(availableBook.getCheckoutTime()).isEqualTo(checkoutTime);
        assertThat(availableBook.getReturnTime()).isEqualTo(returnTime);
    }
}

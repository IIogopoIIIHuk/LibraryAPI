package com.example.LibraryAPI.Controller;

import com.example.LibraryAPI.AvailableBook;
import com.example.LibraryAPI.CheckoutRequest;
import com.example.LibraryAPI.Service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/available-books")
@RequiredArgsConstructor
public class AvailableBookController {

    private final LibraryService libraryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<AvailableBook> getAvailableBooks() {
        return libraryService.getAvailableBooks();
    }

   @PostMapping("/{bookId}/checkout")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> checkoutBook(
            @PathVariable Long bookId,
            @RequestBody CheckoutRequest checkoutRequest // Принимаем данные в теле запроса
    ) {
        libraryService.addAvailableBook(bookId, checkoutRequest.getCheckoutTime(), checkoutRequest.getReturnTime());
        return ResponseEntity.ok("Book checked out successfully");
    }

    @PutMapping("/{bookId}/return")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateReturnTime(@PathVariable Long bookId, @RequestParam LocalDateTime returnTime) {
        libraryService.updateReturnTime(bookId, returnTime);
    }
}

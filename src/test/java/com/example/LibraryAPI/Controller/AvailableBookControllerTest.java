package com.example.LibraryAPI.Controller;

import com.example.LibraryAPI.AvailableBook;
import com.example.LibraryAPI.CheckoutRequest;
import com.example.LibraryAPI.Service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AvailableBookControllerTest {

    @InjectMocks
    private AvailableBookController availableBookController;

    @Mock
    private LibraryService libraryService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(availableBookController).build();
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void testGetAvailableBooks() throws Exception {
        // Arrange
        AvailableBook book1 = new AvailableBook(1L, LocalDateTime.now().minusDays(1), null);
        AvailableBook book2 = new AvailableBook(2L, LocalDateTime.now().minusDays(2), null);
        List<AvailableBook> availableBooks = Arrays.asList(book1, book2);

        when(libraryService.getAvailableBooks()).thenReturn(availableBooks);

        // Act & Assert
        mockMvc.perform(get("/api/available-books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].bookId").value(1L))
                .andExpect(jsonPath("$[1].bookId").value(2L));

        verify(libraryService, times(1)).getAvailableBooks();
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void testCheckoutBook() throws Exception {
        // Arrange
        LocalDateTime checkoutTime = LocalDateTime.now();
        LocalDateTime returnTime = LocalDateTime.now().plusDays(14);
        CheckoutRequest checkoutRequest = new CheckoutRequest(checkoutTime, returnTime);

        // Act & Assert
        mockMvc.perform(post("/api/available-books/{bookId}/checkout", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"checkoutTime\": \"" + checkoutRequest.getCheckoutTime() + "\", \"returnTime\": \"" + checkoutRequest.getReturnTime() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book checked out successfully"));

        verify(libraryService, times(1)).addAvailableBook(eq(1L), eq(checkoutRequest.getCheckoutTime()), eq(checkoutRequest.getReturnTime()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateReturnTime() throws Exception {
        // Arrange
        LocalDateTime returnTime = LocalDateTime.now().plusDays(14);

        // Act & Assert
        mockMvc.perform(put("/api/available-books/{bookId}/return", 1L)
                        .param("returnTime", returnTime.toString()))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).updateReturnTime(eq(1L), eq(returnTime));
    }
}

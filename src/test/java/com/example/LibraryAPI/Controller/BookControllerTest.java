package com.example.LibraryAPI.Controller;

import com.example.LibraryAPI.Book;
import com.example.LibraryAPI.Repo.BookRepository;
import com.example.LibraryAPI.Service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddBook() throws Exception {
        Book book = new Book(1234567890123L, "Test Book", "Test Description", "Test Genre", "Author");

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"Test Book\",\"author\":\"Author\",\"isbn\":1234567890123}"))
                .andExpect(status().isOk());

        verify(bookRepository, times(1)).save(any(Book.class));
        verify(libraryService, times(1)).notifyNewBook(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        Book book = new Book(1234567890123L, "Test Book", "Test Description", "Test Genre", "Author");

        // Добавить книгу в список чтобы будто возвращается одно значение
        books.add(book);

        // Мокируем поведение репозитория
        when(bookRepository.findAll()).thenReturn(books);

        // Выполняем запрос и проверяем ответ
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // Проверка, что массив содержит один элемент
                .andExpect(jsonPath("$[0].title", is("Test Book"))); // Проверка, что title соответствует ожидаемому
    }


    @Test
    @WithMockUser(roles = "USER")
    public void testGetBookByIsbn() throws Exception {
        Book book = new Book(1234567890123L, "Test Book", "Test Description", "Test Genre", "Author");

        when(bookRepository.findByIsbn(1234567890123L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/isbn").param("isbn", "1234567890123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Book")));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetBookById() throws Exception {
        Book book = new Book(1234567890123L, "Test Book", "Test Description", "Test Genre", "Author");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/byId").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Book")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateBook() throws Exception {
        Book book = new Book(1234567890123L, "Test Book", "Test Description", "Test Genre", "Author");

        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        mockMvc.perform(put("/api/books/id-update")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"Updated Book\",\"author\":\"Author\",\"isbn\":1234567890123}"))
                .andExpect(status().isOk())
                .andExpect(content().string(book.toString()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());

        verify(bookRepository, times(1)).deleteById(1L);
    }
}

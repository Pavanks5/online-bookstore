package com.example.online_bookstore.controller;

import com.example.online_bookstore.entity.Book;
import com.example.online_bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        book1 = new Book("Spring Boot Guide", "John Doe", new BigDecimal("19.99"), LocalDate.of(2020, 5, 15));
        book2 = new Book("Hibernate Basics", "Jane Smith", new BigDecimal("25.50"), LocalDate.of(2019, 7, 10));
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<Book> books = Arrays.asList(book1, book2);
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Spring Boot Guide"))
                .andExpect(jsonPath("$[1].title").value("Hibernate Basics"));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById_Found() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Boot Guide"));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testGetBookById_NotFound() throws Exception {
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/99"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBookById(99L);
    }

    @Test
    void testAddBook() throws Exception {
        when(bookService.addBook(any(Book.class))).thenReturn(book1);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Spring Boot Guide\",\"author\":\"John Doe\",\"price\":19.99,\"published_date\":\"2020-05-15\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Spring Boot Guide"));

        verify(bookService, times(1)).addBook(any(Book.class));
    }

    @Test
    void testUpdateBook_Found() throws Exception {
        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(book1);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\",\"author\":\"New Author\",\"price\":20.99,\"published_date\":\"2022-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Boot Guide"));

        verify(bookService, times(1)).updateBook(eq(1L), any(Book.class));
    }

    @Test
    void testUpdateBook_NotFound() throws Exception {
        when(bookService.updateBook(eq(99L), any(Book.class))).thenReturn(null);

        mockMvc.perform(put("/books/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\",\"author\":\"New Author\",\"price\":20.99,\"published_date\":\"2022-01-01\"}"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).updateBook(eq(99L), any(Book.class));
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1L);
    }
}

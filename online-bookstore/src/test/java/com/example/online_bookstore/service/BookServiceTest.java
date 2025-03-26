package com.example.online_bookstore.service;

import com.example.online_bookstore.entity.Book;
import com.example.online_bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book("Spring Boot Guide", "John Doe", new BigDecimal("19.99"), LocalDate.of(2020, 5, 15));
        book2 = new Book("Hibernate Basics", "Jane Smith", new BigDecimal("25.50"), LocalDate.of(2019, 7, 10));
    }

    @Test
    void testAddBook() {
        when(bookRepository.save(book1)).thenReturn(book1);

        Book savedBook = bookService.addBook(book1);

        assertNotNull(savedBook);
        assertEquals("Spring Boot Guide", savedBook.getTitle());
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        Optional<Book> foundBook = bookService.getBookById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Spring Boot Guide", foundBook.get().getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Book> foundBook = bookService.getBookById(99L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    void testUpdateBook_Found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        Book updatedBook = new Book("Updated Spring Boot", "New Author", new BigDecimal("22.99"), LocalDate.now());
        Book result = bookService.updateBook(1L, updatedBook);

        assertNotNull(result);
        assertEquals("Updated Spring Boot", result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Book updatedBook = new Book("Updated Title", "New Author", new BigDecimal("22.99"), LocalDate.now());
        Book result = bookService.updateBook(99L, updatedBook);

        assertNull(result);
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }
}

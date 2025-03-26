package com.example.online_bookstore.controller;

import com.example.online_bookstore.entity.Book;
import com.example.online_bookstore.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  // Import SLF4J Logger
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);  // Logger added

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        logger.info("Received request to add a new book: {}", book.getTitle());
        Book savedBook = bookService.addBook(book);
        logger.info("Book added successfully: {}", savedBook.getId());
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        logger.info("Fetching all books...");
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        logger.info("Fetching book with ID: {}", id);
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            logger.info("Book found: {}", book.get().getTitle());
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else {
            logger.warn("Book with ID {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        logger.info("Updating book with ID: {}", id);
        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook != null) {
            logger.info("Book updated successfully: {}", id);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } else {
            logger.warn("Book with ID {} not found for update", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        logger.info("Deleting book with ID: {}", id);
        bookService.deleteBook(id);
        logger.info("Book deleted successfully: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

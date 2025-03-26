package com.example.online_bookstore.service;

import com.example.online_bookstore.entity.Book;
import com.example.online_bookstore.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        logger.info("Adding a new book: {}", book.getTitle());
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        logger.info("Fetching all books");
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        logger.info("Fetching book with id {}", id);
        return bookRepository.findById(id);
    }

    public Book updateBook(Long id, Book book) {
        logger.info("Updating book with id {}", id);
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book bookToUpdate = existingBook.get();
            bookToUpdate.setTitle(book.getTitle());
            bookToUpdate.setAuthor(book.getAuthor());
            bookToUpdate.setPrice(book.getPrice());
            bookToUpdate.setPublishedDate(book.getPublishedDate());
            return bookRepository.save(bookToUpdate);
        } else {
            logger.error("Book with id {} not found", id);
            return null;
        }
    }

    public void deleteBook(Long id) {
        logger.warn("Deleting book with id {}", id);
        bookRepository.deleteById(id);
    }
}

package com.project.lms.controller;

import com.project.lms.entity.Book;
import com.project.lms.repository.BookRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    // Get all available books

    @GetMapping
    public List<Book> getAvailableBooks() {

        return bookRepository.findByStatus("AVAILABLE");
    }


    // Get book by ID

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {

        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // Add a book

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {

        book.setStatus("AVAILABLE");

        Book savedBook = bookRepository.save(book);

        return ResponseEntity.ok(savedBook);
    }


    // Delete a book

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {

        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        bookRepository.deleteById(id);

        return ResponseEntity.ok("Book deleted successfully");
    }

}
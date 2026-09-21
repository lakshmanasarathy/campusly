package com.project.lms.service;

import org.springframework.beans.factory.annotation.Value;
import com.project.lms.entity.Book;
import com.project.lms.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByStatus("AVAILABLE");
    }

    public Book getBook(Long id) {

        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Book not found"));
    }

    public Book addBook(Book book) {

        book.setStatus("AVAILABLE");

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {

        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }

        bookRepository.deleteById(id);
    }
}
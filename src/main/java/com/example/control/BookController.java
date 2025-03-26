package com.example.control;


import com.example.control.BookController;
import com.example.entity.Book;
import com.example.service.BookService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
	private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    @Autowired
    private BookService bookService;

    @PostMapping
    public Book addBook(@RequestBody Book book) 
    {
    	logger.info("Request has been made to insert book");
    	Book savedBook = bookService.addBook(book);
    	logger.info("Book has been added successfully with ID: {}", book.getId());
        return savedBook;
    }

    @GetMapping
    public List<Book> getAllBooks() {
    	logger.info("Request made to fetch all the books");
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
    	logger.info("Fetching book with ID : {}", id);
        Optional<Book> book = bookService.getBookById(id);
        System.out.println(" ");
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
        	
            Book updatedBook = bookService.updateBook(id, book);
            logger.info("Book with ID {} has been updated successfully", id);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
        	logger.error("The update failed as the book with the given ID doesn't exist");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    	logger.info("Request has been made to delete the book with id : {}",id);
        boolean result = bookService.deleteBook(id);
        if(result == true)
        	logger.info("Book with ID {} has been deleted from the database successfully", id);        return ResponseEntity.noContent().build();
    }
}

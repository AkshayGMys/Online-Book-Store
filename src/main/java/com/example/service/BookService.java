package com.example.service;


import com.example.service.BookService;
import com.example.entity.Book;
import com.example.repository.BookRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.example.exception.BookNotFoundException;
@Service
public class BookService {
	private static final Logger logger=LoggerFactory.getLogger(BookService.class);
    @Autowired
    private BookRepository bookRepository;

    public Book addBook(Book book) {
    	
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
    	List<Book> allBooks = bookRepository.findAll();
    	logger.info("All the books have been fetched successfuly");
        return allBooks;
    }

    public Optional<Book> getBookById(Long id) {
        
        if (!bookRepository.existsById(id)) 
        {
        	logger.error("The book requested does not exist in the Database");
            throw new BookNotFoundException(id);
        }
        logger.info("The book is fetched successfully");
        return bookRepository.findById(id);
    }

    public Book updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id).map(book -> 
        {
        	logger.info("The update process has began for id : {}",id);
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPrice(updatedBook.getPrice());
            book.setPublishedDate(updatedBook.getPublishedDate());
            logger.info("The book has been updated successfully");
            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public boolean deleteBook(Long id) 
    {
    	 if (bookRepository.existsById(id)) 
    	 {
    		 	bookRepository.deleteById(id);
    	        logger.info("The book has been deleted successfully");
    	        return true;
    	 }
    	 else
    	 {
    		 logger.error("The book doesn't exist");
    		 return false;
    	 }
    		 
        
    }
}

package com.example.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.entity.Book;
import com.example.exception.BookNotFoundException;
import com.example.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    //Setting up dummy book which is later added to List
    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Java Training");
        book.setAuthor("Akshay");
        book.setPrice(BigDecimal.valueOf(100));
    }

    //Testing addBook() method
    @Test
    void testAddBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        Book savedBook = bookService.addBook(book);
        assertNotNull(savedBook); //Making sure book object exists 
        assertEquals(book.getId(), savedBook.getId()); //Making sure the book is saved by comparing id's
        verify(bookRepository, times(1)).save(book); //Verifying this method is tested atleast once
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(book); //assing dummy book to dummy list
        when(bookRepository.findAll()).thenReturn(books);
        List<Book> fetchedBooks = bookService.getAllBooks();
        assertFalse(fetchedBooks.isEmpty());//Fetching book failed if fetchedBook is empty
        assertEquals(1, fetchedBooks.size()); //Making sure all books are fetched
        verify(bookRepository, times(1)).findAll(); //Verifying this method is tested atleast once
    }

    @Test
    void testGetBookById_BookExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Optional<Book> foundBook = bookService.getBookById(1L);
        assertTrue(foundBook.isPresent()); //Making sure book is found
        assertEquals(book.getId(), foundBook.get().getId()); //Making sure book found is the book requested for 
        verify(bookRepository, times(1)).findById(1L); //Verifying this method is tested atleast once
    } 

    @Test
    void testGetBookById_BookNotFound() {
        when(bookRepository.existsById(1L)).thenReturn(false);
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L)); //Making sure exception is thrown when book isnt found
        verify(bookRepository, times(1)).existsById(1L); //Verifying this method is tested atleast once
    }

    @Test
    void testUpdateBook_BookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book)); //Making sure true is returned if book is found
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        
        Book updatedBook = new Book(); //Updating the book
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setPrice(BigDecimal.valueOf(1000));
        
        Book result = bookService.updateBook(1L, updatedBook);
        assertEquals("Updated Title", result.getTitle());//Making sure updated values are saved
        assertEquals("Updated Author", result.getAuthor());
        verify(bookRepository, times(1)).save(book); //Verifying this method is tested atleast once
    }

    @Test
    void testUpdateBook_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookService.updateBook(1L, book));//Making sure exception is thrown when book isnt found
        verify(bookRepository, times(1)).findById(1L); //Verifying this method is tested atleast once
    }

    @Test
    void testDeleteBook_BookExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);
        boolean result = bookService.deleteBook(1L);
        assertTrue(result);//Making sure true is returned if book is found
        verify(bookRepository, times(1)).deleteById(1L); //Verifying this method is tested atleast once
    }

    @Test
    void testDeleteBook_BookNotFound() {
        when(bookRepository.existsById(1L)).thenReturn(false);
        boolean result = bookService.deleteBook(1L);
        assertFalse(result);//Making sure false is returned if book is not found
        verify(bookRepository, times(1)).existsById(1L); //Verifying this method is tested atleast once
    }
}

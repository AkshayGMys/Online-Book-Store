package com.example.control;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.entity.Book;
import com.example.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@WebMvcTest(BookController.class)
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private BookService bookService; //Mocks the functionality of service class
    
    @Autowired
    private ObjectMapper objectMapper;//To map java objects to json objects (canot send java objects in the body of HTTP request)
    
    private Book book;
    
    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
    }

    //MockMVC(A new tool ai introduced me to) sends a POST request(HTTP) with the book object in request body to "/books" page.
    //Here the andExpect acts like assert statements.
    @Test
    void testAddBook() throws Exception {
        when(bookService.addBook(any(Book.class))).thenReturn(book); //mocks add book method from service class to return summy book object    
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk()) //Asserts the response is 200 OK
                //The below codes asserts that the json objects have content same as that of java objects
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()));
    }
    
    //MockMVC send a GET request to "/books" to fetch all books
    @Test
    void testGetAllBooks() throws Exception {
        List<Book> books = Arrays.asList(book);
        when(bookService.getAllBooks()).thenReturn(books);
        
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(book.getId()))
                .andExpect(jsonPath("$[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$[0].author").value(book.getAuthor()));
    }
    
  //MockMVC send a GET request to "/book/{id}" to fetch a particular book with particular id
    @Test
    void testGetBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));
        
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()));
    }
    
    
    //MockMVC sends a "PUT" request to update a particular book along with json body with book and its attributes
    @Test
    void testUpdateBook() throws Exception {
        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(book);
     
        mockMvc.perform(put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))//java object to json conversion
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()));
    }
    
    //MockMVC sends a "DELETE" request to delete a particular book
    @Test
    void testDeleteBook() throws Exception {
        when(bookService.deleteBook(1L)).thenReturn(true);
        
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }
}


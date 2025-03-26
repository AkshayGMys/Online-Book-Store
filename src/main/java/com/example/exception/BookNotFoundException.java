package com.example.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.exception.BookNotFoundException;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) 
    {
    	super("The book you are trying to access doesn't exist with ID: " + id);
        final Logger logger = LoggerFactory.getLogger(BookNotFoundException.class);        
        logger.error("BookNotFoundException: ","The book user is trying to access doesn't exist");
    }
}

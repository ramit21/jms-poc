package com.ramit.jms;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.ramit.domain.BookDTO;
import com.ramit.service.BookService;

@Component
public class BookMgrQueueListener {

    private final BookService bookService;

    @Autowired
    public BookMgrQueueListener(BookService bookService) {
        this.bookService = bookService;
    }

    @JmsListener(containerFactory = "containerFactory",
                 destination = "bookMgrQueueDestination",
                 selector = "Operation = 'Create'")
    public void processCreateBookMessage(BookDTO book) throws JMSException{
        bookService.createNew(book);
    }

    @JmsListener(containerFactory = "containerFactory",
                 destination = "bookMgrQueueDestination",
                 selector = "Operation = 'Update'")
    public void processUpdateBookMessage(BookDTO book) throws JMSException{
        bookService.update(book.getIsbn(), book);
    }

    @JmsListener(containerFactory = "containerFactory",
                 destination = "bookMgrQueueDestination",
                 selector = "Operation = 'Delete'")
    public void processDeleteBookMessage(BookDTO book) throws JMSException{
        bookService.delete(book.getIsbn());
    }

}
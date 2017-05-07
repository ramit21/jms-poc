package com.ramit.service;

import org.springframework.stereotype.Service;

import com.ramit.domain.BookDTO;

@Service
public class BookService {

	public void createNew(BookDTO book) {
		System.err.println("Created book : "+book);
	}

	public void update(String isbn, BookDTO book) {
		System.err.println("Udated book : "+book);
	}

	public void delete(String isbn) {
		System.err.println("Deleted book : "+isbn);
	}

	
}

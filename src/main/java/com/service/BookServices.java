package com.service;

import java.util.List;
import com.model.Book;

public interface BookServices {

	List<Book> getAllBooks();
	Book getBookByID(int id);
	boolean addBook(Book book);
	boolean addBookDetails(Book book);
	boolean updateBook(Book book);
	boolean updateBookInfo(Book book);
	boolean deleteBookByID(int id);
	boolean deleteBookDetailsByID(int id);
}

package es.opfind.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import es.opfind.dao.Dao;
import es.opfind.domain.Book;

@Service
public class BookMgr implements Serializable {

	@Resource
	private Dao dao;
	
	public Book newBook(String title, String summary, Date publicationDate) {
		Book book = new Book();
		book.setTitle(title);
		book.setSummary(summary);
		book.setPublicationDate(publicationDate);
		return book;
	}


	
	public void persist(Book book){
		dao.persist(book);
	}
	
	public void persist(Book book[]){
		dao.persist(book);
	}
	
	public List<Book> getBooks(){
		final List<Book> list = dao.find(Book.class);
		return list;
	}
	
	public List<Book> findByFullText(String textToFind) {
		final List<Book> list = dao.findByFullText(Book.class, Book.FULL_TEXT_FIELDS,textToFind);
		return list;
	}
}

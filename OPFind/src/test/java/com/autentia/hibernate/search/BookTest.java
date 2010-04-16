package com.autentia.hibernate.search;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class BookTest {

	private static final Log log = LogFactory.getLog(BookTest.class);

	@Resource
	private Dao dao;

	private void assertFindBooksBy(String textToFind, int expectedBooksFoundCount) {
		final List<Book> books = dao.findByFullText(Book.class, Book.FULL_TEXT_FIELDS, textToFind);
		log.debug("Books found serching by [" + textToFind + "]: " + books);
		assertEquals(expectedBooksFoundCount, books.size());
	}

	@Test
	public void testFullTextQuery() {
		log.trace("Entering");

		final Book[] someBooks = createSomeBooks();
		dao.persist(someBooks);

		assertFindBooksBy("hola", 2);
		assertFindBooksBy("libro sombras", 3);

		// No encuentra 'Ciudades' del 5 libro, el analizador que estamos usando sólo busca palabras completas
		assertFindBooksBy("ciudad", 1);

		// No usamos 'stopwords', así que los artículos, preposiciones, ... también están en el índice
		assertFindBooksBy("los", 2);

		// No encuentra 'Guía' del 5 libro, el analizador que estamos usando considera que no es igual 'i' que 'í'
		assertFindBooksBy("guia", 0);

		// Con esto vemos como los resultados se devuelven ordenados por peso.
		// Es decir, el primer libro que devuelve es el 4, ya que es el único que tiene las dos palabras
		assertFindBooksBy("los en", 4);

		// Una busqueda de fecha. Por defecto la fecha se guarda en foramto yyyyMMddHHmmssSSS, en tiempo GMT
		assertFindBooksBy("20060228", 1);

		// Hemos dicho que guardamos hasta precisión día inlcluido, por lo que, con la configuración actual, no podemos
		// hacer búsquedas menores
		assertFindBooksBy("200802", 0);

		// Por supuesto si podemos mezclar textos y fechas, al fin y al cabo, para lucene todo son textos
		assertFindBooksBy("los en 20080201", 4);

		// Los textos que buscamos son busquedas de Lucene, así que podemos usar toda la potencia de sus operadores.
		// Observe como cambia radicalmente el resultado de la consulta anterior al añadiy un '+'
		assertFindBooksBy("los en +20080201", 1);

		log.trace("Exiting");
	}

	private Book[] createSomeBooks() {
		final Calendar calendar = Calendar.getInstance();
		final Book[] books = new Book[5];

		books[0] = new Book("Hola", "libro sobre los saludos", new Date());

		calendar.set(2008, Calendar.FEBRUARY, 28);
		books[1] = new Book("El libro de las sombras", "lo mejor del ocultismo", calendar.getTime());

		calendar.set(2008, Calendar.FEBRUARY, 1);
		books[2] = new Book("Un asesino en las sombras", "Novela negra", calendar.getTime());

		calendar.set(2005, Calendar.JULY, 28);
		books[3] = new Book("Ciudades de hoy", "Guía de turismo que enseña a decir hola en todos los idiomas", calendar
				.getTime());

		calendar.set(2006, Calendar.FEBRUARY, 28);
		books[4] = new Book("Novela Uno", "Un extraño asesino en serie domina la ciudad", calendar.getTime());

		return books;
	}
}

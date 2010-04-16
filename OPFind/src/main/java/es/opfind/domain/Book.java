package es.opfind.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;

@Entity
@Indexed
public class Book {

	public static final String[] FULL_TEXT_FIELDS = { "title", "summary", "publicationDate" };
	
	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	@DocumentId
	private Integer id;

	@Field
	private String title;

	@Field
	private String summary;

	@Field
	@DateBridge(resolution = Resolution.DAY)
	@Temporal(TemporalType.TIMESTAMP)
	private Date publicationDate;

	public Book() {
		// Default constructor
	}

	public Book(String title, String summary, Date publicationDate) {
		this.title = title;
		this.summary = summary;
		this.publicationDate = publicationDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
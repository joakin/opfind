package es.opfind.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OPNewsletter implements Serializable {

	@SuppressWarnings("unused")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "opnewsletter_id")
	private String id;
	
	private String email;
	
	private Date lastSendDate;
	
	private String searchText;
	
	@Column(unique = true, nullable = false)
	private String uuid;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastSendDate() {
		return lastSendDate;
	}

	public void setLastSendDate(Date lastSendDate) {
		this.lastSendDate = lastSendDate;
	}


	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	
}

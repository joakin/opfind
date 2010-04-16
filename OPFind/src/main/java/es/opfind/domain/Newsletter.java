package es.opfind.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
public class Newsletter implements Serializable {

	@SuppressWarnings("unused")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "newsletter_id")
	private Integer id;

	private String num;
	
	private Date bolDate;
	private String url;

	private Date created;
	
	private Date modified;

	@ManyToOne
	@JoinColumn(name="institution_id", nullable = false)
	private Institution institution;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "newsletter")
	private Set<CivilJob> civilJobs;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Date getBolDate() {
		return bolDate;
	}

	public void setBolDate(Date bolDate) {
		this.bolDate = bolDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public Set<CivilJob> getCivilJobs() {
		return civilJobs;
	}

	public void setCivilJobs(Set<CivilJob> civilJobs) {
		this.civilJobs = civilJobs;
	}
	
	

}

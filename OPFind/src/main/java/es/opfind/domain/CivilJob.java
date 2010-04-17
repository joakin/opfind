package es.opfind.domain;

import java.io.Serializable; 
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;

import es.opfind.controller.civilJob.ListCivilJobs;
import es.opfind.util.StringUtils;

@Entity
@Indexed
public class CivilJob implements Serializable {

	public static final String[] FULL_TEXT_FIELDS = { "description", "fullHtml" };

	@SuppressWarnings("unused")
	@Id
	@DocumentId
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "civiljob_id")
	private Integer id;

	@Column(unique = true, nullable = false)
	private String num;

	@Field
	@Lob
	@Column(columnDefinition = "LongText")
	private String description;
	private String url;

	@Field
	@Lob
	@Column(columnDefinition = "LongText")
	private String fullHtml;
	private String category;


	// Se duplica este campo, pero es necesario indexarlo en esta entidad
	@Field
	@DateBridge(resolution = Resolution.DAY)
	@Temporal(TemporalType.TIMESTAMP)
	private Date bolDate;

	@Basic
	private Date created;

	@Basic
	private Date modified;

	@ManyToOne
	@JoinColumn(name = "newsletter_id", nullable = false)
	private Newsletter newsletter;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFullHtml() {
		return fullHtml;
	}

	public void setFullHtml(String fullHtml) {
		this.fullHtml = fullHtml;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public Newsletter getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(Newsletter newsletter) {
		this.newsletter = newsletter;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getFoundText() {
		
		
		// Esto es una ñapa tremenda

		FacesContext context = FacesContext.getCurrentInstance();
		ValueBinding binding = context.getApplication().createValueBinding("#{listCivilJobs}");
		ListCivilJobs civilJobs = (ListCivilJobs) binding.getValue(context);
		
		//Si no existe en search es que nos lo estan pasando por parametro
		String search = "";
		if (civilJobs.getSearch() != null)
			search = StringUtils.buildLuceneAndQuery(civilJobs.getSearch());
		else {
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			if (request.getParameter("search") != null && !request.getParameter("search").equals("")) {
				search = request.getParameter("search"); 
			}
			
		}
		
		return getFoundText(search);
	
	}
	
	
	
	public String getFoundText(String search){
		return StringUtils.getSummary(search, fullHtml);
	}


	public Date getBolDate() {
		return bolDate;
	}

	public void setBolDate(Date bolDate) {
		this.bolDate = bolDate;
	}


}

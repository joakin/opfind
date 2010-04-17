package es.opfind.controller.civilJob;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import es.opfind.domain.CivilJob;
import es.opfind.domain.Newsletter;
import es.opfind.service.CivilJobMgr;
import es.opfind.service.NewsletterMgr;
import es.opfind.util.StringUtils;

@Controller
@Scope("session")
public class ListCivilJobs implements Serializable {

	@Resource
	private CivilJobMgr civilJobMgr;

	@Resource
	private NewsletterMgr newsletterMgr;

	private String search;

	public List<CivilJob> getCivilJobs() {
		return civilJobMgr.getCivilJobs();
	}

	
	
	public String getReIndex() {

		List<CivilJob> civilJobs = civilJobMgr.getCivilJobs();
		for (CivilJob civilJob : civilJobs) {
			civilJobMgr.persist(civilJob);
		}
		return "";
	}

	public String getResetSearch() {
		search = "";
		return "";
	}

	public String forwardLastSearch() {
		HttpServletRequest request = (HttpServletRequest) this.getFacesContext().getExternalContext().getRequest();

		String afterPath = "/xhtml/civilJob/lastRSS.jsf";
		String beforePath = "http://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();

		try {
			this.getFacesContext().getExternalContext().redirect(beforePath + afterPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}

	public String forwardRssSearch() {

		HttpServletRequest request = (HttpServletRequest) this.getFacesContext().getExternalContext().getRequest();

		// TODO: Verify search has value
		String afterPath = "/xhtml/civilJob/searchRSS.jsf?search=" + StringUtils.buildLuceneAndQuery(search);
		String beforePath = "http://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		try {
			this.getFacesContext().getExternalContext().redirect(beforePath + afterPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}

	public Set<CivilJob> getLastCivilJobs() {
		List<Newsletter> newsletters = newsletterMgr.getNewslettersOrderByBolDate();
		if (!newsletters.equals("")) {
			return newsletters.get(0).getCivilJobs();
		}
		return new HashSet<CivilJob>();
	}

	public String getSearch() {
		return search;
	}

	public String getHighSearch(){
		HttpServletRequest request = (HttpServletRequest) this.getFacesContext().getExternalContext().getRequest();
		
		if (request.getParameter("search") != null && !request.getParameter("search").equals("")) 
			return request.getParameter("search");
		
		if ( search != null && !search.equals(""))
			return StringUtils.buildLuceneAndQuery(search);
		
		return "";
		
		 
	}
	
	public String getShareUrl(){
		
		HttpServletRequest request = (HttpServletRequest) this.getFacesContext().getExternalContext().getRequest();
		
		String afterPath = null;
		
		if ( search != null && !search.equals(""))
			afterPath = "/xhtml/civilJob/search.jsf?search=" + StringUtils.buildLuceneAndQuery(search);
		else
			afterPath = "/xhtml/index.jsf";
		
		String beforePath = "http://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		
		return beforePath + afterPath;
		
	}
	
	public void setSearch(String search) {
		this.search = search;
	}

	/**
	 * Process search String for lucene
	 * 
	 * @param event
	 */
	public void setLuceneSearch(ActionEvent event) {

	}

	private FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public List<CivilJob> getLuceneSearch() {

		HttpServletRequest request = (HttpServletRequest) this.getFacesContext().getExternalContext().getRequest();

		if (request.getParameter("search") != null && !request.getParameter("search").equals("")) {
			return civilJobMgr.findByFullTextAndSortDate(request.getParameter("search"));
		}

		if (search != null && !search.equals(""))
			return civilJobMgr.findByFullTextAndSortDate(StringUtils.buildLuceneAndQuery(search));
		else
			return new ArrayList<CivilJob>();
	}

}

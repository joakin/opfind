package es.opfind.controller.civilJob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

import es.opfind.domain.CivilJob;
import es.opfind.domain.Newsletter;
import es.opfind.service.CivilJobMgr;
import es.opfind.service.NewsletterMgr;
import es.opfind.util.MessageBundle;

@Controller
@Scope("session")
public class RssCivilJobs {

	private enum RssType{
		GENERAL,
		SEARCH
	}
	
	private String search;
	
	@Resource
	private NewsletterMgr newsletterMgr;
	
	@Resource CivilJobMgr civilJobMgr;
	
	
	public String getLastRss() {
		List<Newsletter> newsletters = newsletterMgr.getNewslettersOrderByBolDate();
		
		Set<CivilJob> civilJobs = new HashSet<CivilJob>();
		if ( !newsletters.equals("")){
			civilJobs = newsletters.get(0).getCivilJobs();
		}
		
		return createFeedByCivilJobs(civilJobs,RssType.GENERAL);
	}
	
	public String getSearchRss(){
		HttpServletRequest request = (HttpServletRequest) this.getFacesContext().getExternalContext().getRequest();
		
		List<CivilJob> civilJobs = new ArrayList<CivilJob>(); 
		
		if ( request.getParameter("search")!= null && !request.getParameter("search").equals("") ){
			civilJobs = civilJobMgr.findByFullTextAndSortDate(request.getParameter("search"));
			search = request.getParameter("search");
			
		}
		if ( civilJobs.size() > 100 ){
			return createFeedByCivilJobs(civilJobs.subList(0, 100),RssType.SEARCH);
		}
		else
			return createFeedByCivilJobs(civilJobs,RssType.SEARCH);
		
	}
	
	private SyndFeed createFeedHeader(String title, String link, String description){
		SyndFeed feed = new SyndFeedImpl();
		feed.setFeedType("rss_2.0");
		
		feed.setTitle(title);
		feed.setLink(link);
		feed.setDescription(description);
		
		UIViewRoot view = getFacesContext().getViewRoot();
		Locale locale = view.getLocale();
		feed.setLanguage(locale.getLanguage());
		
		return feed;
	}
	
	private String createFeedByCivilJobs(Collection<CivilJob> civilJobs,RssType rssType){
		
		HttpServletRequest request = (HttpServletRequest) this.getFacesContext().getExternalContext().getRequest();
		
		SyndFeed feed = null;
		
		switch (rssType) {
		case GENERAL:
			feed = createFeedHeader(MessageBundle.getMessageFromResourceBundle("generalrsstitle", getFacesContext()), 
					"http://www.opfind.org", MessageBundle.getMessageFromResourceBundle("generalrssdescription", getFacesContext()));	
			break;

		case SEARCH:
			feed = createFeedHeader(MessageBundle.getMessageFromResourceBundle("searchrsstitle", getFacesContext()) + search,  
					"http://www.opfind.org", MessageBundle.getMessageFromResourceBundle("searchrssdescription", getFacesContext()));			
			break;
		}
		

		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		
			for (CivilJob civilJob : civilJobs) {
				SyndEntry entry;
				SyndContent description;

				entry = new SyndEntryImpl();
				entry.setTitle(civilJob.getNum());
				
				entry.setLink("http://" + request.getServerName()+":"+request.getServerPort() +request.getContextPath() + 
						"/xhtml/civilJob/item.jsf?num="+civilJob.getNum());
				entry.setPublishedDate(civilJob.getBolDate());
				description = new SyndContentImpl();
				description.setType("text/html");

				switch (rssType) {
				case GENERAL:
					description.setValue("<p>"+ civilJob.getDescription() +"</p>");	
					break;

				case SEARCH:
					description.setValue("<p>"+  civilJob.getDescription() +"</p></br><p>" + civilJob.getFoundText(search) +"</p>");			
					break;
				}

				entry.setDescription(description);
				entries.add(entry);
			
		}
		
		feed.setEntries(entries);

		//return ("success");
		SyndFeedOutput output = new SyndFeedOutput();
			
		try {
			return output.outputString(feed);
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private FacesContext getFacesContext() { 
		return FacesContext.getCurrentInstance();
	}
	
}

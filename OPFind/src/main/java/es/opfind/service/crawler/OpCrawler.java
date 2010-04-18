package es.opfind.service.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ccil.cowan.tagsoup.Parser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import es.opfind.dao.GenericDao;
import es.opfind.domain.CivilJob;
import es.opfind.domain.Institution;
import es.opfind.domain.Newsletter;
import es.opfind.service.CivilJobMgr;
import es.opfind.service.InstitutionMgr;
import es.opfind.service.NewsletterMgr;
import es.opfind.util.XMLUtils;

/**
 * 
 * @author Joan Navarrete Sanchez
 * 
 */

@Service
public class OpCrawler implements Serializable {

	private static final Log log = LogFactory.getLog(GenericDao.class);
	private static final String BASE_DOMAIN = "http://www.boe.es/";
	private Institution institution;
	private Newsletter newsletter;
	private Date date;

	@Resource
	private InstitutionMgr institutionMgr;

	@Resource
	private NewsletterMgr newsletterMgr;

	@Resource
	private CivilJobMgr civilJobMgr;

	public void storeCivilJobs(Date date) {
		this.date = date;
		storeCivilJobs();
	}

	public void storeCivilJobs() {
		try {
			if (date == null)
				date = new Date();

			// Initialize institution
			this.institution = initInstitution();

			Document mainDocument = getDom4jDocument(getBOEUrl());
			this.newsletter = extractNewsletter(mainDocument);
			this.newsletter.setInstitution(institution);
			
			Map<String,String> lis = extractLis(mainDocument);
			
			Set<CivilJob> civilJobs = extractCivilJobs(lis);
			
			this.newsletter.setCivilJobs(civilJobs);
			
			newsletterMgr.persist(newsletter);
			

		} catch (Exception e) {
			log.error(e);
		}
	}

	private Boolean wasCrawled(Date currentDate) {

		List<Newsletter> newsletters = newsletterMgr.getNewslettersOrderByBolDate();
		Date lastDate = null;

		if (!newsletters.isEmpty()) {
			lastDate = newsletters.get(0).getBolDate();
			if (currentDate.equals(lastDate))
				return true;
			else if (currentDate.after(lastDate))
				return false;
		}

		return false;
	}

	private Institution initInstitution() {
		
		List<Institution> institutions = institutionMgr.getInstitutionByName("Boletín Oficial del Estado");
		
		if (institutions.isEmpty()){
			institution = institutionMgr.newInstitution();
			institution.setName("Boletín Oficial del Estado");
			institutionMgr.persist(institution);
			return institution;
		}

		return institutions.get(0);
	}

	/**
	 * Obtain a Dom4j document by an url
	 * 
	 * @param url
	 * @return
	 */
	private Document getDom4jDocument(URL url) {
		Document document = null;
		try {
			XMLReader r = new Parser();

			URLConnection conn = url.openConnection();

			((Parser) r).setFeature("http://xml.org/sax/features/namespace-prefixes", true);
			SAXReader reader = new SAXReader(r);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			document = reader.read(br);

		} catch (SAXNotSupportedException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} catch (SAXNotRecognizedException e) {
			log.error(e);
		} catch (DocumentException e) {
			log.error(e);
		}
		return document;
	}

	/**
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	private URL getBOEUrl() throws MalformedURLException {
		// Format the date
		Format formatter = new SimpleDateFormat("yyyy");
		String year = formatter.format(date);

		formatter = new SimpleDateFormat("MM");
		String month = formatter.format(date);

		formatter = new SimpleDateFormat("dd");
		String day = formatter.format(date);

		return new URL(BASE_DOMAIN + "boe/dias/" + year + "/" + month + "/" + day + "/");

	}

	private Set<CivilJob> extractCivilJobs(Map<String,String> lis){
		
		Set<CivilJob> civilJobs = new HashSet<CivilJob>();
		
		for (Entry<String, String> entry: lis.entrySet()) {
			try {
				CivilJob job = extractCivilJob(getDom4jDocument(new URL(BASE_DOMAIN + entry.getValue())),entry.getKey());
				if (job != null){
					job.setNewsletter(newsletter);
					job.setBolDate(newsletter.getBolDate());
					civilJobs.add(job);
				}
			} catch (MalformedURLException e) {
				log.error(e);
			}
		}
		
		return civilJobs;
	}
	
	/**
	 * 
	 * @param document
	 * @return
	 * @throws MalformedURLException
	 */
	private CivilJob extractCivilJob(Document document,String num) throws MalformedURLException {

		Node node = document.selectSingleNode("//html:div[@id='DOdoc']/html:h3/html:span");
		if (node != null) {
			String title = XMLUtils.getNodeText(node);
			Pattern patron = Pattern.compile(".+osicion.+oncurso.+");
			Matcher match = patron.matcher(title);

			if (match.find()) {
				try {

					Node description = node.selectSingleNode("//html:p[@class='documento-tit']");
					Node url = node.selectSingleNode("//html:li[@class='puntoPDF']/html:a");
					Node fullText = node.selectSingleNode("//html:div[@id='textoxslt']");
					Node category = node.selectSingleNode("//html:div[@id='DOdoc']/html:h4");

					return civilJobMgr.newCivilJob(XMLUtils.getNodeText(description), BASE_DOMAIN + XMLUtils.getNodeAttributeText(
							url, "href"), XMLUtils.writeNode(fullText), XMLUtils.getNodeText(category),num);
				} catch (Exception e) {
					log.error(e);
				}
			}
		}

		return null;

	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws MalformedURLException
	 */
	private Newsletter extractNewsletter(Document document) throws MalformedURLException {

		Node sumario = document.selectSingleNode("//html:ul[@class='linkSumario']/html:li/html:a");
		Pattern sumPattern = Pattern.compile(".+BOE-\\w-\\d+-");
		Matcher sumMatch = sumPattern.matcher(XMLUtils.getNodeAttributeText(sumario, "title"));
		String num = sumMatch.replaceAll("");

		return newsletterMgr.newNewsletter(num, date, getBOEUrl().toString());

	}

	/**
	 * 
	 * @param document
	 * @return
	 * @throws MalformedURLException
	 */
	private Map<String, String> extractLis(Document document) throws MalformedURLException {

		Map<String, String> lis = new HashMap<String, String>();
		List<Node> list = document.selectNodes("//html:li[@class='puntoMas']/html:a");

		for (Node entry : list) {
			try {
				Element element = (Element) entry;
				Pattern patron = Pattern.compile("BOE-\\w-\\d+-\\d+");
				Matcher match = patron.matcher(element.attributeValue("title"));
				match.find();
				lis.put(element.attributeValue("title").substring(match.start(), match.end()), element
						.attributeValue("href"));
			} catch (Exception e) {
				log.error(e);
			}
		}
		return lis;
	}

}

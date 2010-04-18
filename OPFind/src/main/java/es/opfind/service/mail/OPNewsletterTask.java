package es.opfind.service.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.ccil.cowan.tagsoup.Parser;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import es.opfind.domain.CivilJob;
import es.opfind.domain.OPNewsletter;
import es.opfind.service.CivilJobMgr;
import es.opfind.service.NewsletterMgr;
import es.opfind.service.OPNewsletterMgr;
import es.opfind.util.XMLUtils;

@Service
public class OPNewsletterTask implements Serializable {

	@Resource
	private NewsletterMgr newsletterMgr;

	@Resource
	private OPNewsletterMgr opNewsletterMgr;

	@Resource
	private CivilJobMgr civilJobMgr;

	public void sendOneMail() {

		sendNews();
	}

	public void sendNews() {

		URL url = this.getClass().getResource("NEWSLETTER.html");
		
		Document document = XMLUtils.getDom4jDocument(url);
		
		XMLUtils.writeNode(document);
		
		Node node = document.selectSingleNode("//html:table[@id='table']");
		
		List<OPNewsletter> opNewslettes = opNewsletterMgr.getOPNewsletters();

		List<Node> nodes = new ArrayList<Node>();
		
		Properties properties = initProperties();
		
		for (OPNewsletter opNewsletter : opNewslettes) {
			List<CivilJob> civilJobs = civilJobMgr.getUpdatesByDateAndSearch(opNewsletter.getLastSendDate(),
					opNewsletter.getSearchText());

			for (CivilJob civilJob : civilJobs) {
				Element element = document.getRootElement().createCopy();
				Node node2 = createRows(civilJob, element, opNewsletter);
				nodes.add(node2);
			}
			
			for (Node node2 : nodes) {
				
				sendMail(properties, "Nuevas Oposiciones", XMLUtils.writeNode(node), "news@opfind.es", opNewsletter.getEmail());
			}
		}
		
		

	}

	
	private Element createElemnt(String element,Attribute[] attributes){
		Element creElement = DocumentHelper.createElement(element);
		for (int i = 0; i < attributes.length; i++) {
			creElement.addAttribute(attributes[i].getName(), attributes[i].getValue());
		}
		
		return creElement;
	} 
	
	private Node createRows(CivilJob civilJob,Node node,OPNewsletter newsletter){
		
		URL url = this.getClass().getResource("NEWSLETTER_MAIN.html");
		
		Document document = XMLUtils.getDom4jDocument(url);
		
		
		Node tr1 = document.selectSingleNode("//html:tr[@id='1']");
		
		
		Node font11 = document.selectSingleNode("//html:font[@id='11']");
		font11.setText(civilJob.getCategory());
		
		Node font12 = document.selectSingleNode("//html:font[@id='12']");
		font12.setText(civilJob.getBolDate().toGMTString());
		
		
		
		Node tr2 = document.selectSingleNode("//html:tr[@id='2']");
		
		Node span21 = document.selectSingleNode("//html:span[@id='21']");
		span21.setText(civilJob.getDescription());
		
		
		Node span31 = document.selectSingleNode("//html:span[@id='31']");
		span31.setText(civilJob.getFoundText(newsletter.getSearchText()));
		
		
		
		Node tr3 = document.selectSingleNode("//html:tr[@id='3']");
		
		
		Node tr5 = document.selectSingleNode("//html:tr[@id='5']");
		
		Node font51 = document.selectSingleNode("//html:a[@id='51']");
		((Element)font51).addAttribute("href", "http://www.opfind.org/xhtml/civilJob/item.jsf?num=" + civilJob.getNum());
		
		Node font52 = document.selectSingleNode("//html:font[@id='52']");
		
		Node font53 = document.selectSingleNode("//html:a[@id='53']");
		((Element)font53).addAttribute("href", civilJob.getUrl());
		
		Node font54 = document.selectSingleNode("//html:font[@id='54']");
		font54.setText("Ver" + civilJob.getNum());
		
		((Element)node).add(((Element)tr1).createCopy());
		((Element)node).add(((Element)tr2).createCopy());
		((Element)node).add(((Element)tr3).createCopy());
		((Element)node).add(((Element)tr5).createCopy());
		
		XMLUtils.writeNode(node);
		return node;
		
	}


	private void sendMail(Properties properties, String subject, String html, String from, String to) {

		Session session = Session.getDefaultInstance(properties);

		MimeMessage message = new MimeMessage(session);

		// Quien envia el correo
		try {
			message.setFrom(new InternetAddress(from));

			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			message.setSubject(subject);
			message.setText("UTF-8", "html");

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Transport t;
		try {
			t = session.getTransport("smtp");
			t.connect("opfind.es@gmail.com", "2PnowG2i");
			t.sendMessage(message, message.getAllRecipients());
			t.close();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Properties initProperties() {
		Properties properties = new Properties();
		try {
			URL url = this.getClass().getResource("mail.properties");
			properties.load(url.openStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return properties;
	}

}

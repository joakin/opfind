package es.opfind.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ccil.cowan.tagsoup.Parser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import es.opfind.service.crawler.OpCrawler;

public class XMLUtils {

	private static final Log log = LogFactory.getLog(XMLUtils.class);
	
	public static String getNodeText(Node node) {
		if (node != null && node.getText() != null)
			return node.getText();
		else
			return "";
	}

	/**
	 * Obtain a Dom4j document by an url
	 * 
	 * @param url
	 * @return
	 */
	public static Document getDom4jDocument(URL url) {
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
	
	public static String getNodeAttributeText(Node node, String qName) {
		if (node != null && node instanceof Element && ((Element) node).attribute(qName) != null
				&& ((Element) node).attributeValue(qName) != null)
			return ((Element) node).attributeValue(qName);
		else
			return "";
	}

	public static String writeNode(Node node) {
		String result = null;
		try {
			OutputFormat formato = OutputFormat.createPrettyPrint();
			formato.setIndent(true);
			formato.setXHTML(true);
			formato.setSuppressDeclaration(true);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			org.dom4j.io.XMLWriter output = new org.dom4j.io.XMLWriter(stream, formato);
			output.write(node);
			output.close();
			result = new String( stream.toByteArray(), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}

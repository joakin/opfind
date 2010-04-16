package es.opfind.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;

public class XMLUtils {

	public static String getNodeText(Node node) {
		if (node != null && node.getText() != null)
			return node.getText();
		else
			return "";
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

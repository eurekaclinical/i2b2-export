package edu.emory.cci.aiw.i2b2datadownloader.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public final class XmlUtil {
	public static Object evalXPath(Node d, String expr, QName returnType)
			throws XPathExpressionException {
		return XPathFactory.newInstance().newXPath().compile(expr)
				.evaluate(d, returnType);
	}

	public static Object evalXPath(String xml, String expr, QName returnType)
			throws XPathExpressionException, SAXException, IOException,
			ParserConfigurationException {
		return evalXPath(xmlStringToDocument(xml), expr, returnType);
	}

	public static String xmlDocumentToString(Node d)
			throws TransformerFactoryConfigurationError, TransformerException {
		StringWriter writer = new StringWriter();
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.transform(new DOMSource(d), new StreamResult(writer));
		return writer.toString();
	}

	public static Document xmlStringToDocument(String xml) throws SAXException,
			IOException, ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(new StringReader(xml)));
	}

	public static void main(String[] args) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		String xml = "<message_header>" + " <security>"
				+ "     <username>user</username>"
				+ "     <password isSession=\"true\" valid=\"false\">pass</password>"
				+ "     <domain>domain.com</domain>" + " </security>"
				+ "</message_header>";
		Node secNode = (Node) evalXPath(xml,
				"//message_header/security/password", XPathConstants.NODE);
		for (int i = 0; i < secNode.getAttributes().getLength(); i++) {
			Node node = secNode.getAttributes().item(i);
			System.out.println(node.getNodeName() + "|" + node.getNodeValue());
		}
	}
}

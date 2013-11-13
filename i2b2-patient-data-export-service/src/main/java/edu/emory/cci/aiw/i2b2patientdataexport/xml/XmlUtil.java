package edu.emory.cci.aiw.i2b2patientdataexport.xml;

/*
 * #%L
 * i2b2 Patient Data Export Service
 * %%
 * Copyright (C) 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

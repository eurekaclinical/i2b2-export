package edu.emory.bmi.aiw.i2b2export.xml;

/*
 * #%L
 * i2b2 Export Service
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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Utility methods for handling XPaths and converting between XML document objects and strings.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public final class XmlUtil {

	private XmlUtil() {
		// to prevent instantiation
	}

	/**
	 * Gets the object at the given XPath in the given XML node. See also: {@link #evalXPath(String, String, javax.xml.namespace.QName)}.
	 *
	 * @param d the node to look in
	 * @param expr the XPath expression to evaluate
	 * @param returnType the expected return type of the object
	 * @return the object at the specified XPath in the node
	 * @throws XPathExpressionException if an error occurs processing the XPath expression
	 */
	public static Object evalXPath(Node d, String expr, QName returnType) throws XPathExpressionException {
		return XPathFactory.newInstance().newXPath().compile(expr)
				.evaluate(d, returnType);
	}

	/**
	 * Gets the object at the given XPath in the given XML string. See also: {@link #evalXPath(org.w3c.dom.Node, String, javax.xml.namespace.QName)}.
	 *
	 * @param xml the XML string to look in
	 * @param expr the XPath expression to evaluate
	 * @param returnType expected return type of the object
	 * @return the object at the specified XPath in the XML string
	 * @throws XPathExpressionException if an error occurs while processing the XPath expression
	 * @throws SAXException if an error occurs converting the XML string to an XML node
	 * @throws IOException if a general IO error occurs
	 * @throws ParserConfigurationException if an error occurs configuring the parser
	 */
	public static Object evalXPath(String xml, String expr, QName returnType) throws XPathExpressionException,
			SAXException, IOException, ParserConfigurationException {
		return evalXPath(xmlStringToDocument(xml), expr, returnType);
	}

	/**
	 * Converts an XML document or node to an equivalent string. Applies the reverse operation of {@link #xmlStringToDocument(String)}.
	 *
	 * @param d the node to convert
	 * @return a String representation of the XML node
	 * @throws TransformerFactoryConfigurationError if an error occurs configuring the transformer factory
	 * @throws TransformerException if an error occurs during the transformation
	 */
	public static String xmlDocumentToString(Node d) throws TransformerFactoryConfigurationError, TransformerException {
		StringWriter writer = new StringWriter();
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.transform(new DOMSource(d), new StreamResult(writer));
		return writer.toString();
	}

	/**
	 * Converts an XML string into an equivalent XML document. Applies the reverse operation of {@link #xmlDocumentToString(org.w3c.dom.Node)}.
	 *
	 * @param xml the XML string to convert
	 * @return a {@link Document} equivalent to the specified XML string
	 * @throws SAXException if an error occurs building the XML document
	 * @throws IOException if a general IO error occurs
	 * @throws ParserConfigurationException if an error occurs configuring the XML parser
	 */
	public static Document xmlStringToDocument(String xml) throws SAXException, IOException, ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(new StringReader(xml)));
	}
}

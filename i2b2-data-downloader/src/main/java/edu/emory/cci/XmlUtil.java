package edu.emory.cci;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

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

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

final class XmlUtil {
    static Object evalXPath(Document d, String expr, QName returnType)
            throws XPathExpressionException {
        return XPathFactory.newInstance().newXPath().compile(expr)
                .evaluate(d, returnType);
    }

    static Object evalXPath(String xml, String expr, QName returnType)
            throws XPathExpressionException, SAXException, IOException,
            ParserConfigurationException {
        return evalXPath(xmlStringToDocument(xml), expr, returnType);
    }

    static String xmlDocumentToString(Document d)
            throws TransformerFactoryConfigurationError, TransformerException {
        StringWriter writer = new StringWriter();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.transform(new DOMSource(d), new StreamResult(writer));
        return writer.toString();
    }

    static Document xmlStringToDocument(String xml) throws SAXException,
            IOException, ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new InputSource(new StringReader(xml)));
    }
}

package edu.emory.cci.aiw.i2b2datadownloader.xml;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import junit.framework.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class OutputColumnConfigurationXmlParserTest {


    public void testParseCommon() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        String colConfigXml = "<columnConfiguration>" +
                "<order>2</order>" +
                "<concept>\\\\i2b2\\Concepts\\MyConcept</concept>" +
                "<columnName>My Concept</columnName>" +
                "<displayFormat>existence</displayFormat>" +
                "<howMany></howMany>" +
                "<includeUnits></includeUnits>" +
                "<includeTimeRange></includeTimeRange>" +
                "<aggregation></aggregation>" +
                "</columnConfiguration>";
        Document d = XmlUtil.xmlStringToDocument(colConfigXml);
        OutputColumnConfiguration colConfig = new OutputColumnConfigurationXmlParser(d).parse();

        Assert.assertEquals(2, colConfig.getOrder().intValue());
        Assert.assertEquals("\\\\i2b2\\Concepts\\MyConcept", colConfig.getI2b2Concept());
        Assert.assertEquals("My Concept", colConfig.getColumnName());
    }


    public void testParseExistence() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        String colConfigXml = "<columnConfiguration>" +
                "<order>2</order>" +
                "<concept>\\\\i2b2\\Concepts\\MyConcept</concept>" +
                "<columnName>My Concept</columnName>" +
                "<displayFormat>existence</displayFormat>" +
                "<howMany></howMany>" +
                "<includeUnits></includeUnits>" +
                "<includeTimeRange></includeTimeRange>" +
                "<aggregation></aggregation>" +
                "</columnConfiguration>";
        Document d = XmlUtil.xmlStringToDocument(colConfigXml);
        OutputColumnConfiguration colConfig = new OutputColumnConfigurationXmlParser(d).parse();

        Assert.assertEquals(OutputColumnConfiguration.DisplayFormat.EXISTENCE, colConfig.getDisplayFormat());
    }


    public void testParseValue() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        String colConfigXml = "<columnConfiguration>" +
                "<order>2</order>" +
                "<concept>\\\\i2b2\\Concepts\\MyConcept</concept>" +
                "<columnName>My Concept</columnName>" +
                "<displayFormat>value</displayFormat>" +
                "<howMany>4</howMany>" +
                "<includeUnits>true</includeUnits>" +
                "<includeTimeRange>false</includeTimeRange>" +
                "<aggregation></aggregation>" +
                "</columnConfiguration>";
        Document d = XmlUtil.xmlStringToDocument(colConfigXml);
        OutputColumnConfiguration colConfig = new OutputColumnConfigurationXmlParser(d).parse();

        Assert.assertEquals(OutputColumnConfiguration.DisplayFormat.VALUE, colConfig.getDisplayFormat());
        Assert.assertEquals(4, colConfig.getHowMany().intValue());
        Assert.assertEquals(Boolean.TRUE, colConfig.getIncludeUnits());
        Assert.assertEquals(Boolean.FALSE, colConfig.getIncludeTimeRange());
    }


    public void testParseAggMin() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        String colConfigXml = "<columnConfiguration>" +
                "<order>2</order>" +
                "<concept>\\\\i2b2\\Concepts\\MyConcept</concept>" +
                "<columnName>My Concept</columnName>" +
                "<displayFormat>aggregation</displayFormat>" +
                "<howMany></howMany>" +
                "<includeUnits>true</includeUnits>" +
                "<includeTimeRange></includeTimeRange>" +
                "<aggregation>min</aggregation>" +
                "</columnConfiguration>";
        Document d = XmlUtil.xmlStringToDocument(colConfigXml);
        OutputColumnConfiguration colConfig = new OutputColumnConfigurationXmlParser(d).parse();

        Assert.assertEquals(OutputColumnConfiguration.DisplayFormat.AGGREGATION, colConfig.getDisplayFormat());
        Assert.assertEquals(Boolean.TRUE, colConfig.getIncludeUnits());
        Assert.assertEquals(OutputColumnConfiguration.AggregationType.MIN, colConfig.getAggregation());
    }


    public void testParseAggMax() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        String colConfigXml = "<columnConfiguration>" +
                "<order>2</order>" +
                "<concept>\\\\i2b2\\Concepts\\MyConcept</concept>" +
                "<columnName>My Concept</columnName>" +
                "<displayFormat>aggregation</displayFormat>" +
                "<howMany></howMany>" +
                "<includeUnits>true</includeUnits>" +
                "<includeTimeRange></includeTimeRange>" +
                "<aggregation>max</aggregation>" +
                "</columnConfiguration>";
        Document d = XmlUtil.xmlStringToDocument(colConfigXml);
        OutputColumnConfiguration colConfig = new OutputColumnConfigurationXmlParser(d).parse();

        Assert.assertEquals(OutputColumnConfiguration.DisplayFormat.AGGREGATION, colConfig.getDisplayFormat());
        Assert.assertEquals(Boolean.TRUE, colConfig.getIncludeUnits());
        Assert.assertEquals(OutputColumnConfiguration.AggregationType.MAX, colConfig.getAggregation());
    }


    public void testParseAggAvg() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        String colConfigXml = "<columnConfiguration>" +
                "<order>2</order>" +
                "<concept>\\\\i2b2\\Concepts\\MyConcept</concept>" +
                "<columnName>My Concept</columnName>" +
                "<displayFormat>aggregation</displayFormat>" +
                "<howMany></howMany>" +
                "<includeUnits>true</includeUnits>" +
                "<includeTimeRange></includeTimeRange>" +
                "<aggregation>avg</aggregation>" +
                "</columnConfiguration>";
        Document d = XmlUtil.xmlStringToDocument(colConfigXml);
        OutputColumnConfiguration colConfig = new OutputColumnConfigurationXmlParser(d).parse();

        Assert.assertEquals(OutputColumnConfiguration.DisplayFormat.AGGREGATION, colConfig.getDisplayFormat());
        Assert.assertEquals(Boolean.TRUE, colConfig.getIncludeUnits());
        Assert.assertEquals(OutputColumnConfiguration.AggregationType.AVG, colConfig.getAggregation());
    }


    public void testParseGarbage() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
                String colConfigXml = "<columnConfiguration>" +
                "<order>second</order>" +
                "<concept>\\\\i2b2\\Concepts\\MyConcept</concept>" +
                "<columnName>My Concept</columnName>" +
                "<displayFormat>value</displayFormat>" +
                "<howMany>-3</howMany>" +
                "<includeUnits>foo</includeUnits>" +
                "<includeTimeRange>bar</includeTimeRange>" +
                "<aggregation></aggregation>" +
                "</columnConfiguration>";
        Document d = XmlUtil.xmlStringToDocument(colConfigXml);
        OutputColumnConfiguration colConfig = new OutputColumnConfigurationXmlParser(d).parse();

        Assert.assertEquals(Integer.MAX_VALUE, colConfig.getOrder().intValue());
        Assert.assertEquals(1, colConfig.getHowMany().intValue());
        Assert.assertEquals(Boolean.FALSE, colConfig.getIncludeUnits());
        Assert.assertEquals(Boolean.FALSE, colConfig.getIncludeTimeRange());
    }
}

package edu.emory.cci.aiw.i2b2patientdataexport.xml;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public class OutputColumnConfigurationXmlParser {
	private final Node colConfigNode;

	OutputColumnConfigurationXmlParser(Node columnConfigurationNode) {
		this.colConfigNode = columnConfigurationNode;
		try {
			String xml = XmlUtil.xmlDocumentToString(colConfigNode);
			System.out.println(xml);
		} catch (TransformerException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	OutputColumnConfiguration parse() throws XPathExpressionException {
		OutputColumnConfiguration colConfig = new OutputColumnConfiguration();

		try {
			String orderStr = (String) XmlUtil.evalXPath(colConfigNode, "//order", XPathConstants.STRING);
			Integer order = Integer.parseInt(orderStr);
			colConfig.setColumnOrder(order);
		} catch (NumberFormatException e) {
			colConfig.setColumnOrder(Integer.MAX_VALUE);
		}

		String concept = (String) XmlUtil.evalXPath(colConfigNode, "//concept", XPathConstants.STRING);
		//colConfig.setI2b2Concept(concept);

		String colName = (String) XmlUtil.evalXPath(colConfigNode, "//columnName", XPathConstants.STRING);
		colConfig.setColumnName(colName);

		String dispFmt = (String) XmlUtil.evalXPath(colConfigNode, "//displayFormat", XPathConstants.STRING);
		if (dispFmt.equalsIgnoreCase("existence")) {
			colConfig.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.EXISTENCE);
		} else if (dispFmt.equalsIgnoreCase("value")) {
			colConfig.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);
		} else if (dispFmt.equalsIgnoreCase("aggregation")) {
			colConfig.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.AGGREGATION);
		} else {
			throw new AssertionError("invalid display format provided: " + dispFmt);
		}

		if (colConfig.getDisplayFormat() == OutputColumnConfiguration.DisplayFormat.VALUE) {
			try {
				Integer howMany = Integer.parseInt((String) XmlUtil.evalXPath(colConfigNode, "//howMany", XPathConstants.STRING));
				if (howMany < 1) {
					colConfig.setHowMany(1);
				} else {
					colConfig.setHowMany(howMany);
				}
			} catch (NumberFormatException e) {
				colConfig.setHowMany(1);
			}

			String timeRange = (String) XmlUtil.evalXPath(colConfigNode, "//includeTimeRange", XPathConstants.STRING);
			if (timeRange.equalsIgnoreCase("true")) {
				colConfig.setIncludeTimeRange(true);
			} else {
				colConfig.setIncludeTimeRange(false);
			}
		}

		if (colConfig.getDisplayFormat() == OutputColumnConfiguration.DisplayFormat.AGGREGATION) {
			String aggType = (String) XmlUtil.evalXPath(colConfigNode, "//aggregation", XPathConstants.STRING);
			if (aggType.equalsIgnoreCase("min")) {
				colConfig.setAggregation(OutputColumnConfiguration.AggregationType.MIN);
			} else if (aggType.equalsIgnoreCase("max")) {
				colConfig.setAggregation(OutputColumnConfiguration.AggregationType.MAX);
			} else if (aggType.equalsIgnoreCase("avg")) {
				colConfig.setAggregation(OutputColumnConfiguration.AggregationType.AVG);
			} else {
				throw new AssertionError("invalid aggregation type provided: " + aggType);
			}
		}

		if (colConfig.getDisplayFormat() == OutputColumnConfiguration.DisplayFormat.VALUE || colConfig.getDisplayFormat() == OutputColumnConfiguration.DisplayFormat.AGGREGATION) {
			String units = (String) XmlUtil.evalXPath(colConfigNode, "//includeUnits", XPathConstants.STRING);
			if (units.equalsIgnoreCase("true")) {
				colConfig.setIncludeUnits(true);
			} else {
				colConfig.setIncludeUnits(false);
			}
		}

		return colConfig;
	}
}

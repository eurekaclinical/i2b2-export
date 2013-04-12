package edu.emory.cci.aiw.i2b2patientdataexport.xml;

import edu.emory.cci.aiw.i2b2patientdataexport.I2b2PatientDataExportServiceXmlException;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public final class OutputConfigurationXmlParser {
	private final Document requestXml;

	public OutputConfigurationXmlParser(Document requestXml) {
		this.requestXml = requestXml;
	}

	public OutputConfiguration parse() throws I2b2PatientDataExportServiceXmlException {
		try {
			OutputConfiguration result = new OutputConfiguration();

			String configName = (String) XmlUtil.evalXPath(requestXml, "//outputConfiguration/name", XPathConstants.STRING);
			result.setName(configName);

			String rowDim = (String) XmlUtil.evalXPath(requestXml, "//outputConfiguration/rowDimension", XPathConstants.STRING);
			if (rowDim.equalsIgnoreCase("patient")) {
				result.setRowDimension(OutputConfiguration.RowDimension.PATIENT);
			} else if (rowDim.equalsIgnoreCase("visit")) {
				result.setRowDimension(OutputConfiguration.RowDimension.VISIT);
			} else if (rowDim.equalsIgnoreCase("provider")) {
				result.setRowDimension(OutputConfiguration.RowDimension.PROVIDER);
			} else {
				throw new AssertionError("invalid row dimension provided: " + rowDim);
			}

			String whitespace = (String) XmlUtil.evalXPath(requestXml, "//outputConfiguration/whitespaceReplacement", XPathConstants.STRING);
			result.setWhitespaceReplacement(whitespace);

			String colSep = (String) XmlUtil.evalXPath(requestXml, "//outputConfiguration/columnSeparator", XPathConstants.STRING);
			result.setSeparator(colSep);

			String missing = (String) XmlUtil.evalXPath(requestXml, "//outputConfiguration/missingValues", XPathConstants.STRING);
			result.setMissingValue(missing);

			List<OutputColumnConfiguration> colConfigs = new ArrayList<OutputColumnConfiguration>();
			NodeList colConfigsXml = (NodeList) XmlUtil.evalXPath(requestXml, "//outputConfiguration/columnConfigurations/columnConfiguration", XPathConstants.NODESET);
			int l = colConfigsXml.getLength();
			for (int i = 0; i < colConfigsXml.getLength(); i++) {
				Node n = colConfigsXml.item(i);
				OutputColumnConfigurationXmlParser colConfigParser = new OutputColumnConfigurationXmlParser(colConfigsXml.item(i));
				colConfigs.add(colConfigParser.parse());
			}
			result.setColumnConfigs(colConfigs);

			return result;
		} catch (XPathExpressionException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		}
	}
}

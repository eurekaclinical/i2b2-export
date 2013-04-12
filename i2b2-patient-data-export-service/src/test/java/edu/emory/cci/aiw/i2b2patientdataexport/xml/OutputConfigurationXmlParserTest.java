package edu.emory.cci.aiw.i2b2patientdataexport.xml;

import edu.emory.cci.aiw.i2b2patientdataexport.I2b2PatientDataExportServiceXmlException;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import junit.framework.Assert;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class OutputConfigurationXmlParserTest {

	private static final String HEADER =
			"<i2b2:request xmlns:i2b2=\"http://www.i2b2.org/xsd/hive/msg/1.1/\" xmlns:pm=\"http://www.i2b2.org/xsd/cell/pm/1.1/\">" +
					"<message_header>" +
					"        <i2b2_version_compatible>1.1</i2b2_version_compatible>" +
					"<hl7_version_compatible>2.4</hl7_version_compatible>" +
					"<sending_application>" +
					"			<application_name>i2b2 Project Management</application_name>" +
					"			<application_version>1.1</application_version>" +
					"</sending_application>" +
					"		<sending_facility>" +
					"			<facility_name>i2b2 Hive</facility_name>" +
					"		</sending_facility>" +
					"		<receiving_application>" +
					"			<application_name>Project Management Cell</application_name>" +
					"			<application_version>1.1</application_version>" +
					"		</receiving_application>" +
					"		<receiving_facility>" +
					"			<facility_name>i2b2 Hive</facility_name>" +
					"		</receiving_facility>" +
					"		<datetime_of_message>${messageDatetime}</datetime_of_message>" +
					"		<security>	" +
					"		${securityNode}" +
					"		</security>" +
					"    	<message_control_id>" +
					"			<message_num>${messageId}</message_num>" +
					"			<instance_num>0</instance_num>" +
					"		</message_control_id>" +
					"		<processing_id>" +
					"			<processing_id>P</processing_id>" +
					"			<processing_mode>I</processing_mode>" +
					"		</processing_id>" +
					"		<accept_acknowledgement_type>AL</accept_acknowledgement_type>" +
					"		<application_acknowledgement_type>AL</application_acknowledgement_type>" +
					"		<country_code>US</country_code>" +
					"       <project_id>${projectId}</project_id>" +
					"	</message_header>" +
					"	<request_header>" +
					"		<result_waittime_ms>180000</result_waittime_ms>" +
					"	</request_header>" +
					"   <message_body>" +
					"        <datadownloader>";

	private static final String FOOTER = "</datadownloader></message_body></i2b2:request>";


	public void testParse() throws ParserConfigurationException, SAXException, IOException, I2b2PatientDataExportServiceXmlException {
		String configXml = "<outputConfiguration>" +
				"<name>foo</name>" +
				"<rowDimension>patient</rowDimension>" +
				"<whitespaceReplacement>_</whitespaceReplacement>" +
				"<columnSeparator>,</columnSeparator>" +
				"<missingValues>(NULL)</missingValues>" +
				"<columnConfigurations>" +
				"<columnConfiguration>" +
				"<order>1</order>" +
				"<concept>\\\\i2b2\\Concepts\\MyConcept1</concept>" +
				"<columnName>col1</columnName>" +
				"<displayFormat>existence</displayFormat>" +
				"<howMany></howMany>" +
				"<includeUnits></includeUnits>" +
				"<includeTimeRange></includeTimeRange>" +
				"<aggregation></aggregation>" +
				"</columnConfiguration>" +
				"<columnConfiguration>" +
				"<order>1</order>" +
				"<concept>\\\\i2b2\\Concepts\\MyConcept1</concept>" +
				"<columnName>col1</columnName>" +
				"<displayFormat>existence</displayFormat>" +
				"<howMany></howMany>" +
				"<includeUnits></includeUnits>" +
				"<includeTimeRange></includeTimeRange>" +
				"<aggregation></aggregation>" +
				"</columnConfiguration>" +
				"</columnConfigurations>" +
				"</outputConfiguration>";

		OutputConfiguration config = new OutputConfigurationXmlParser(XmlUtil.xmlStringToDocument(configXml)).parse();

		Assert.assertEquals("foo", config.getName());
		Assert.assertEquals(OutputConfiguration.RowDimension.PATIENT, config.getRowDimension());
		Assert.assertEquals("_", config.getWhitespaceReplacement());
		Assert.assertEquals(",", config.getSeparator());
		Assert.assertEquals("(NULL)", config.getMissingValue());
		Assert.assertEquals(2, config.getColumnConfigs().size());
	}
}

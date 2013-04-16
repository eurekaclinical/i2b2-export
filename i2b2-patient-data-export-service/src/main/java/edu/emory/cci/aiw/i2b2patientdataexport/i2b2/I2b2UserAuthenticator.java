package edu.emory.cci.aiw.i2b2patientdataexport.i2b2;

import edu.emory.cci.aiw.i2b2patientdataexport.xml.I2b2PatientDataExportServiceXmlException;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2patientdataexport.xml.XmlUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class I2b2UserAuthenticator {

	private final Configuration config;
	private final I2b2AuthMetadata authMetadata;

	/**
	 * Creates a new I2b2UserAuthenticator instance based on the given XML.
	 *
	 * @param authMetadata the metadata used to authenticate to i2b2
	 */
	public I2b2UserAuthenticator(I2b2AuthMetadata authMetadata) {
		this.authMetadata = authMetadata;
		this.config = new Configuration();
		this.config.setClassForTemplateLoading(this.getClass(), "/");
		this.config.setObjectWrapper(new DefaultObjectWrapper());
	}

	/**
	 * Authenticates an i2b2 user.
	 *
	 * @return <code>true</code> if the user was authenticated,
	 *         <code>false</code> otherwise
	 * @throws edu.emory.cci.aiw.i2b2patientdataexport.xml.I2b2PatientDataExportServiceXmlException if an error occurred in the parsing of the incoming or
	 *                                    response XML
	 */
	public boolean authenticateUser() throws I2b2PatientDataExportServiceXmlException {
		try {
			Template tmpl = this.config.getTemplate(I2b2CommUtil.TEMPLATES_DIR + "/i2b2_user_auth.ftl");
			StringWriter writer = new StringWriter();

			DateFormat sdf = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);
			Date now = new Date();
			String messageId = I2b2CommUtil.generateMessageId();

			Map<String, Object> params = new HashMap<String, Object>();
            params.put("redirectHost", I2b2CommUtil.I2B2_SERVICE_HOST_URL);
			params.put("domain", this.authMetadata.getDomain());
			params.put("username", this.authMetadata.getUsername());
			params.put("passwordNode", this.authMetadata.getPasswordNode());
			params.put("messageId", messageId);
			params.put("messageDatetime", sdf.format(now));
			params.put("projectId", this.authMetadata.getProjectId());

			tmpl.process(params, writer);
			Document respXml = I2b2CommUtil.postXmlToI2b2(writer.toString());

			String status = (String) XmlUtil.evalXPath(respXml,
					"//response_header/result_status/status/@type",
					XPathConstants.STRING);

			return "DONE".equalsIgnoreCase(status);
		} catch (IOException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (XPathExpressionException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (SAXException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (ParserConfigurationException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (TemplateException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		}

	}
}

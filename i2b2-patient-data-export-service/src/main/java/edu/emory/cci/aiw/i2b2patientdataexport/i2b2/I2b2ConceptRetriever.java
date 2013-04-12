package edu.emory.cci.aiw.i2b2patientdataexport.i2b2;

import edu.emory.cci.aiw.i2b2patientdataexport.I2b2PatientDataExportServiceXmlException;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2patientdataexport.xml.XmlUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.http.client.ClientProtocolException;
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

public final class I2b2ConceptRetriever {

	private static final String CONCEPT_XPATH_PREFIX = "//message_body/concepts/concept/";

	private final Configuration config;
	private final DateFormat sdf;
	private final I2b2AuthMetadata authMetadata;


	public I2b2ConceptRetriever(I2b2AuthMetadata authMetadata) throws I2b2PatientDataExportServiceXmlException {
		this.authMetadata = authMetadata;
		this.config = new Configuration();
		this.config.setClassForTemplateLoading(this.getClass(), "/");
		this.config.setObjectWrapper(new DefaultObjectWrapper());
		this.sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	}

    public I2b2Concept retrieveConceptByCode(String code) throws I2b2PatientDataExportServiceXmlException {
        try {
            Template tmpl = this.config.getTemplate("i2b2_ont_codeinfo.ftl");
            StringWriter writer = new StringWriter();

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
            params.put("conceptCode", code);

            tmpl.process(params, writer);
            Document respXml = I2b2CommUtil.postXmlToI2b2(writer.toString());

            return extractConcept(respXml);
        } catch (SAXException e) {
            throw new I2b2PatientDataExportServiceXmlException(e);
        } catch (TemplateException e) {
            throw new I2b2PatientDataExportServiceXmlException(e);
        } catch (ParserConfigurationException e) {
            throw new I2b2PatientDataExportServiceXmlException(e);
        } catch (XPathExpressionException e) {
            throw new I2b2PatientDataExportServiceXmlException(e);
        } catch (ClientProtocolException e) {
            throw new I2b2PatientDataExportServiceXmlException(e);
        } catch (IOException e) {
            throw new I2b2PatientDataExportServiceXmlException(e);
        }
    }

	public I2b2Concept retrieveConceptByPath(String conceptPath) throws I2b2PatientDataExportServiceXmlException {
		try {
			Template tmpl = this.config.getTemplate("i2b2_ont_terminfo.ftl");
			StringWriter writer = new StringWriter();

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
			params.put("conceptPath", conceptPath);

			tmpl.process(params, writer);
			Document respXml = I2b2CommUtil.postXmlToI2b2(writer.toString());

			return extractConcept(respXml);
		} catch (IOException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (TemplateException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (IllegalStateException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (SAXException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (ParserConfigurationException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (XPathExpressionException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		}
	}

	private I2b2Concept extractConcept(Document respXml) throws XPathExpressionException {
		int level = Integer.parseInt((String) XmlUtil.evalXPath(respXml, CONCEPT_XPATH_PREFIX + "level", XPathConstants.STRING));
		String key = (String) XmlUtil.evalXPath(respXml, CONCEPT_XPATH_PREFIX + "key", XPathConstants.STRING);
		String tableName = (String) XmlUtil.evalXPath(respXml, CONCEPT_XPATH_PREFIX + "tablename", XPathConstants.STRING);
		String dimensionCode = (String) XmlUtil.evalXPath(respXml, CONCEPT_XPATH_PREFIX + "dimcode", XPathConstants.STRING);
		String isSynonym = (String) XmlUtil.evalXPath(respXml, CONCEPT_XPATH_PREFIX + "synonym_cd", XPathConstants.STRING);

		return new I2b2Concept(key, level, tableName, dimensionCode, isSynonym);
	}
}

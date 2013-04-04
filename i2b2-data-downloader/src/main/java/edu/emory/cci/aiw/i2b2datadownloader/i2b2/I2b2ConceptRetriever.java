package edu.emory.cci.aiw.i2b2datadownloader.i2b2;

import edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderXmlException;
import edu.emory.cci.aiw.i2b2datadownloader.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2datadownloader.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2datadownloader.xml.XmlUtil;
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

public final class I2b2ConceptRetriever {
	private static final String I2B2_ONT_URL = I2b2CommUtil.I2B2_BASE_URL + "rest/OntologyService/getTermInfo";
	private static final String CONCEPT_XPATH_PREFIX = "//message_body/concepts/concept/";

	private final Configuration config;
	private final DateFormat sdf;
	private final I2b2AuthMetadata authMetadata;


	public I2b2ConceptRetriever(I2b2AuthMetadata authMetadata) throws DataDownloaderXmlException {
		this.authMetadata = authMetadata;
		this.config = new Configuration();
		this.config.setClassForTemplateLoading(this.getClass(), "/");
		this.config.setObjectWrapper(new DefaultObjectWrapper());
		this.sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	}

	public I2b2Concept retrieveConcept(String conceptPath) throws DataDownloaderXmlException {
		try {
			Template tmpl = this.config.getTemplate("i2b2_ont_terminfo.ftl");
			StringWriter writer = new StringWriter();

			Date now = new Date();
			String messageId = I2b2CommUtil.generateMessageId();

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("domain", this.authMetadata.getDomain());
			params.put("username", this.authMetadata.getUsername());
			params.put("passwordNode", this.authMetadata.getPasswordNode());
			params.put("messageId", messageId);
			params.put("messageDatetime", sdf.format(now));
			params.put("projectId", this.authMetadata.getProjectId());
			params.put("conceptPath", conceptPath);

			tmpl.process(params, writer);
			Document respXml = I2b2CommUtil.postXmlToI2b2(I2B2_ONT_URL,
					writer.toString());

			return extractConcept(respXml);
		} catch (IOException e) {
			throw new DataDownloaderXmlException(e);
		} catch (TemplateException e) {
			throw new DataDownloaderXmlException(e);
		} catch (IllegalStateException e) {
			throw new DataDownloaderXmlException(e);
		} catch (SAXException e) {
			throw new DataDownloaderXmlException(e);
		} catch (ParserConfigurationException e) {
			throw new DataDownloaderXmlException(e);
		} catch (XPathExpressionException e) {
			throw new DataDownloaderXmlException(e);
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

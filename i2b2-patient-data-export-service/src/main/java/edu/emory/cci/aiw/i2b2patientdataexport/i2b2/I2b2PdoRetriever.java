package edu.emory.cci.aiw.i2b2patientdataexport.i2b2;

import edu.emory.cci.aiw.i2b2patientdataexport.xml.I2b2PatientDataExportServiceXmlException;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2PatientSet;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.I2b2PdoResultParser;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.I2b2PdoResults;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class I2b2PdoRetriever {

	private final Configuration config;
	private final I2b2PatientSet i2b2PatientSet;
	private final I2b2AuthMetadata i2b2AuthMetadata;

	public I2b2PdoRetriever(I2b2AuthMetadata i2b2AuthMetadata,
							I2b2PatientSet i2b2PatientSet) {
		this.i2b2AuthMetadata = i2b2AuthMetadata;
		this.i2b2PatientSet = i2b2PatientSet;
		this.config = new Configuration();
		this.config.setClassForTemplateLoading(this.getClass(), "/");
		this.config.setObjectWrapper(new DefaultObjectWrapper());
	}

	public I2b2PdoResults retrieve(Collection<I2b2Concept> concepts) throws I2b2PatientDataExportServiceXmlException {
		try {
			Template tmpl = this.config.getTemplate(I2b2CommUtil.TEMPLATES_DIR + "i2b2_pdo_request.ftl");
			StringWriter writer = new StringWriter();

			String messageId = I2b2CommUtil.generateMessageId();

			Map<String, Object> params = new HashMap<String, Object>();
            params.put("redirectHost", I2b2CommUtil.I2B2_SERVICE_HOST_URL);
			params.put("domain", i2b2AuthMetadata.getDomain());
			params.put("username", i2b2AuthMetadata.getUsername());
			params.put("passwordNode", i2b2AuthMetadata.getPasswordNode());
			params.put("messageId", messageId);
			params.put("projectId", i2b2AuthMetadata.getProjectId());
			params.put("patientListMax", i2b2PatientSet.getPatientSetSize());
			params.put("patientListMin", "1");
			params.put("patientSetCollId", i2b2PatientSet.getPatientSetCollId());
			params.put("items", concepts);

			tmpl.process(params, writer);
			Document respXml = I2b2CommUtil.postXmlToI2b2(writer.toString());
			I2b2PdoResultParser parser = new I2b2PdoResultParser(respXml);
			return parser.parse();
		} catch (IOException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (TemplateException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (SAXException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (ParserConfigurationException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		}
	}
}
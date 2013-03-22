package edu.emory.cci.aiw.i2b2datadownloader.i2b2;

import edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderXmlException;
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
    private static final String I2B2_ONT_URL = "http://192.168.86.129/i2b2/rest/OntologyService/getTermInfo";
    private static final String CONCEPT_XPATH_PREFIX = "//message_body/concepts/concept/";

    private final Configuration config;
    private final DateFormat sdf;
    private final String xml;

    private String projectId;
    private String security;

    I2b2ConceptRetriever(String xml) throws DataDownloaderXmlException {
        this.xml = xml;
        this.config = new Configuration();
        this.config.setClassForTemplateLoading(this.getClass(), "/");
        this.config.setObjectWrapper(new DefaultObjectWrapper());
        this.sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        extractFields();
    }

    private void extractFields() throws DataDownloaderXmlException {
        try {
            this.projectId = I2b2CommUtil.extractProjectId(this.xml);
            this.security = I2b2CommUtil.extractSecurityNode(this.xml);
        } catch (XPathExpressionException e) {
            throw new DataDownloaderXmlException(e);
        } catch (SAXException e) {
            throw new DataDownloaderXmlException(e);
        } catch (IOException e) {
            throw new DataDownloaderXmlException(e);
        } catch (ParserConfigurationException e) {
            throw new DataDownloaderXmlException(e);
        }
    }

    public I2b2Concept retrieveConcept(String conceptPath) throws DataDownloaderXmlException {
        try {
            Template tmpl = this.config.getTemplate("i2b2_ont_terminfo.ftl");
            StringWriter writer = new StringWriter();

            Date now = new Date();
            String messageId = I2b2CommUtil.generateMessageId();

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("messageId", messageId);
            params.put("messageDatetime", sdf.format(now));
            params.put("projectId", this.projectId);
            params.put("securityNode", this.security);
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

    public static void main(String[] args) throws DataDownloaderXmlException {
        StringWriter w = new StringWriter();
        w.write("\\\\i2b2\\Diagnoses\\Resp");
        String s = w.toString();
        System.out.println(w.toString());
        String xml = "<ns6:request xmlns:ns4=\"http://www.i2b2.org/xsd/cell/crc/psm/1.1/\" xmlns:ns7=\"http://www.i2b2.org/xsd/cell/crc/psm/querydefinition/1.1/\" xmlns:ns3=\"http://www.i2b2.org/xsd/cell/crc/pdo/1.1/\" xmlns:ns5=\"http://www.i2b2.org/xsd/hive/plugin/\" xmlns:ns2=\"http://www.i2b2.org/xsd/hive/pdo/1.1/\" xmlns:ns6=\"http://www.i2b2.org/xsd/hive/msg/1.1/\"><message_header><proxy><redirect_url>http://192.168.86.129/i2b2/rest/QueryToolService/pdorequest</redirect_url></proxy><sending_application><application_name>i2b2_QueryTool</application_name><application_version>0.2</application_version></sending_application><sending_facility><facility_name>PHS</facility_name></sending_facility><receiving_application><application_name>i2b2_DataRepositoryCell</application_name><application_version>0.2</application_version></receiving_application><receiving_facility><facility_name>PHS</facility_name></receiving_facility><message_type><message_code>Q04</message_code><event_type>EQQ</event_type></message_type><security><domain>i2b2demo</domain><username>i2b2</username><password token_ms_timeout=\"1800000\" is_token=\"true\">SessionKey:nhYuVSgVtb0G1H944mFY</password></security><message_control_id><message_num>eyVKlfO00mIBxc2E1ue73</message_num><instance_num>0</instance_num></message_control_id><processing_id><processing_id>P</processing_id><processing_mode>I</processing_mode></processing_id><accept_acknowledgement_type>messageId</accept_acknowledgement_type><project_id>Demo2</project_id></message_header><request_header><result_waittime_ms>180000</result_waittime_ms></request_header>" +
                "<message_body></message_body></ns6:request>";
        I2b2ConceptRetriever r = new I2b2ConceptRetriever(xml);
        System.out.println(r.retrieveConcept("\\\\i2b2\\i2b2\\Diagnoses\\Respiratory system (460-519)\\Chronic obstructive diseases (490-496)\\(493) Asthma"));
    }
}

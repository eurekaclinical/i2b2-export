package edu.emory.cci;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

final class I2b2ConceptRetriever {
    private static final String I2B2_ONT_URL = "http://localhost:9090/i2b2/rest/OntologyService/getTermInfo";

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

    void retrieveConcept(String conceptPath) throws DataDownloaderXmlException {
        try {
            Template tmpl = this.config.getTemplate("i2b2_ont_terminfo.ftl");
            StringWriter writer = new StringWriter();

            Date now = new Date();
            String messageId = I2b2CommUtil.generateMessageId();

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("messageId", messageId);
            params.put("messageDatatime", sdf.format(now));
            params.put("projectId", this.projectId);
            params.put("securityNode", this.security);

            tmpl.process(params, writer);
            Document respXml = I2b2CommUtil.postXmlToI2b2(I2B2_ONT_URL,
                    writer.toString());
            
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
        }
    }
}

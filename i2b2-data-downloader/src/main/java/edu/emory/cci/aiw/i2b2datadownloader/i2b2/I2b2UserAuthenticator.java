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

public final class I2b2UserAuthenticator {

    private static final String I2B2_PM_URL = "http://localhost:9090/i2b2/rest/PMService/getServices";

    private final String xml;
    private final Configuration config;

    private String projectId;
    private String security;
    
    /**
     * Creates a new I2b2UserAuthenticator instance based on the given XML.
     * 
     * @param xml
     *            the i2b2 request XML. All of the user authentication
     *            parameters will be pulled from here.
     * @throws DataDownloaderXmlException if the XML is malformed or incomplete
     */
    public I2b2UserAuthenticator(String xml) throws DataDownloaderXmlException {
        this.xml = xml;
        this.config = new Configuration();
        this.config.setClassForTemplateLoading(this.getClass(), "/");
        this.config.setObjectWrapper(new DefaultObjectWrapper());
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

    /**
     * Authenticates an i2b2 user.
     * 
     * @return <code>true</code> if the user was authenticated,
     *         <code>false</code> otherwise
     * @throws DataDownloaderXmlException
     *             if an error occurred in the parsing of the incoming or
     *             response XML
     */
    public boolean authenticateUser() throws DataDownloaderXmlException {
        try {
            Template tmpl = this.config.getTemplate("i2b2_user_auth.ftl");
            StringWriter writer = new StringWriter();

            DateFormat sdf = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);
            Date now = new Date();
            String messageId = I2b2CommUtil.generateMessageId();

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("messageId", messageId);
            params.put("messageDatetime", sdf.format(now));
            params.put("projectId", this.projectId);
            params.put("securityNode", this.security);

            tmpl.process(params, writer);
            Document respXml = I2b2CommUtil.postXmlToI2b2(I2B2_PM_URL,
                    writer.toString());

            String status = (String) XmlUtil.evalXPath(respXml,
                    "//response_header/result_status/status/@type",
                    XPathConstants.STRING);

            return "DONE".equalsIgnoreCase(status);
        } catch (IOException e) {
            throw new DataDownloaderXmlException(e);
        } catch (XPathExpressionException e) {
            throw new DataDownloaderXmlException(e);
        } catch (SAXException e) {
            throw new DataDownloaderXmlException(e);
        } catch (ParserConfigurationException e) {
            throw new DataDownloaderXmlException(e);
        } catch (TemplateException e) {
            throw new DataDownloaderXmlException(e);
        }

    }
}

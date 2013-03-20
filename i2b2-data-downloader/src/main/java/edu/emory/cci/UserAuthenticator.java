package edu.emory.cci;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

final class UserAuthenticator {

    private static final String I2B2_PM_URL = "http://localhost:9090/i2b2/rest/PMService/getServices";

    private final String xml;
    private final Configuration config;

    /**
     * Creates a new UserAuthenticator instance based on the given XML.
     * 
     * @param xml
     *            the i2b2 request XML. All of the user authentication
     *            parameters will be pulled from here.
     */
    UserAuthenticator(String xml) {
        this.xml = xml;
        this.config = new Configuration();
        this.config.setClassForTemplateLoading(this.getClass(), "/");
        this.config.setObjectWrapper(new DefaultObjectWrapper());
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
    boolean authenticateUser() throws DataDownloaderXmlException {
        Template tmpl;
        try {
            tmpl = this.config.getTemplate("i2b2_user_auth.ftl");
            StringWriter writer = new StringWriter();

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date now = new Date();
            String messageId = I2b2CommUtil.generateMessageId();

            String projectId = (String) XmlUtil.evalXPath(this.xml,
                    "//message_header/project_id", XPathConstants.STRING);
            String securityNode = (String) XmlUtil.evalXPath(this.xml,
                    "//message_header/security", XPathConstants.STRING);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("messageId", messageId);
            params.put("messageDatetime", sdf.format(now));
            params.put("projectId", projectId);
            params.put("securityNode", securityNode);

            tmpl.process(params, writer);
            String respXml = I2b2CommUtil.postXmlToI2b2(I2B2_PM_URL,
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

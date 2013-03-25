package edu.emory.cci.aiw.i2b2datadownloader.i2b2;

import edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderXmlException;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.I2b2PdoResultParser;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Patient;
import freemarker.template.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public final class I2b2PdoRetriever {

    private static final String I2B2_PDO_URL = "http://172.16.89.128/i2b2/rest/QueryToolService/pdorequest";
    private final Configuration config;

    private String security = "<domain>i2b2demo</domain>\n" +
			"<username>i2b2</username>\n" +
			"<password token_ms_timeout=\"1800000\" is_token=\"true\">SessionKey:X9SDD4dSdOuWTv5gOCk0</password>";
    private String projectId = "Demo2";

    public I2b2PdoRetriever() {
        this.config = new Configuration();
        this.config.setClassForTemplateLoading(this.getClass(), "/");
        this.config.setObjectWrapper(new DefaultObjectWrapper());
    }

    public List<Patient> retrieve(Collection<I2b2Concept> concepts) throws DataDownloaderXmlException {
        try {
            Template tmpl = this.config.getTemplate("i2b2_pdo_request.ftl");
            StringWriter writer = new StringWriter();

            String messageId = I2b2CommUtil.generateMessageId();

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("messageId", messageId);
            params.put("projectId", projectId);
            params.put("securityNode", security);
            params.put("patientSetLimit", "100");
            params.put("patientListMax", "1");
            params.put("patientListMin", "6");
            params.put("patientSetCollId", "81");
            params.put("items", concepts);

            tmpl.process(params, writer);
            Document respXml = I2b2CommUtil.postXmlToI2b2(I2B2_PDO_URL, writer.toString());
            List<Patient> patients = I2b2PdoResultParser.parse(respXml);

            return patients;
        } catch (IOException e) {
            throw new DataDownloaderXmlException(e);
        } catch (TemplateException e) {
            throw new DataDownloaderXmlException(e);
        } catch (SAXException e) {
            throw new DataDownloaderXmlException(e);
        } catch (ParserConfigurationException e) {
            throw new DataDownloaderXmlException(e);
        } catch (XPathExpressionException e) {
            throw new DataDownloaderXmlException(e);
        }
    }

    public static void main(String[] args) throws DataDownloaderXmlException {
        I2b2PdoRetriever r = new I2b2PdoRetriever();
        I2b2Concept c1 = new I2b2Concept("\\\\i2b2\\i2b2\\Diagnoses\\Circulatory system (390-459)\\", 2, "concept_dimension", "\\i2b2\\Diagnoses\\Circulatory system (390-459)\\", "N");
        I2b2Concept c2 = new I2b2Concept("key2", 4, "table2", "dim2", "N");
        Collection<I2b2Concept> concepts = new HashSet<I2b2Concept>();
        concepts.add(c1);
//        concepts.add(c2);
        r.retrieve(concepts);
    }
}

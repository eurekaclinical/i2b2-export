package edu.emory.cci.aiw.i2b2patientdataexport.i2b2;

import edu.emory.cci.aiw.i2b2patientdataexport.xml.XmlUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

public final class I2b2CommUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(I2b2CommUtil.class);

//    private static final String I2B2_PROXY_URL = "https://eureka-dev.cci.emory.edu/i2b2/index.php";
//    static final String I2B2_SERVICE_HOST_URL = "http://localhost:9090";

    private static final String I2B2_PROXY_URL = "http://192.168.232.128/webclient/index.php";
    static final String I2B2_SERVICE_HOST_URL = "http://localhost:9090";

    static final String TEMPLATES_DIR = "i2b2-xml-templates";

	/*
	 * The i2b2 date format. The colon (:) must be stripped out of the timezone portion first.
	 */
	public static final String I2B2_DATE_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	/**
	 * Performs an HTTP POST of an XML request to the i2b2 proxy cell. Request XML must contain
     * the redirect_url node with the address of the actual service endpoint to call
     * in order for the request to be properly routed.
	 *
     * @param xml the XML request to send
	 * @return the XML response from i2b2 as a {@link Document}
	 * @throws ClientProtocolException      if an error occurs
	 * @throws IOException                  if an error occurs
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IllegalStateException
	 */
	public static Document postXmlToI2b2(String xml)
			throws ClientProtocolException, IOException, IllegalStateException,
			SAXException, ParserConfigurationException {
        LOGGER.debug("POSTing XML: " + xml);
		HttpClient http = new DefaultHttpClient();
		HttpPost i2b2Post = new HttpPost(I2B2_PROXY_URL);
		StringEntity xmlEntity = new StringEntity(xml);
		xmlEntity.setContentType("text/xml");
		i2b2Post.setEntity(xmlEntity);
		HttpResponse resp = http.execute(i2b2Post);

        if (LOGGER.isDebugEnabled()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            StringBuilder respXml = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                respXml.append(line);
            }
            LOGGER.debug("Response XML: " + respXml.toString());

            return XmlUtil.xmlStringToDocument(respXml.toString());
        } else {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(resp.getEntity().getContent());
        }
	}

	/**
	 * Generates a random message number for i2b2 requests. Copied from the i2b2
	 * SMART project: https://community.i2b2.org/wiki/display/SMArt/SMART+i2b2
	 * in class: edu.harvard.i2b2.smart.ws.SmartAuthHelper
	 *
	 * @return a unique message ID
	 */
	public static String generateMessageId() {
		StringWriter strWriter = new StringWriter();
		for (int i = 0; i < 20; i++) {
			int num = getValidAcsiiValue();
			strWriter.append((char) num);
		}
		return strWriter.toString();
	}

	private static int getValidAcsiiValue() {
		int number = 48;
		while (true) {
			number = 48 + (int) Math.round(Math.random() * 74);
			if ((number > 47 && number < 58) || (number > 64 && number < 91)
					|| (number > 96 && number < 123)) {
				break;
			}
		}
		return number;
	}
}

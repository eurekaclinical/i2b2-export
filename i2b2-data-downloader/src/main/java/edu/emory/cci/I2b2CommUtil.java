package edu.emory.cci;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

final class I2b2CommUtil {

    /**
     * Performs an HTTP POST of an XML request to an i2b2 instance
     * 
     * @param url
     *            the address of the i2b2 instance to POST to
     * @param xml
     *            the XML request to send
     * @return the XML response from i2b2 as a {@link Document}
     * @throws ClientProtocolException
     *             if an error occurs
     * @throws IOException
     *             if an error occurs
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IllegalStateException
     */
    static Document postXmlToI2b2(String url, String xml)
            throws ClientProtocolException, IOException, IllegalStateException,
            SAXException, ParserConfigurationException {
        HttpClient http = new DefaultHttpClient();
        HttpPost i2b2Post = new HttpPost(url);
        StringEntity xmlEntity = new StringEntity(xml);
        xmlEntity.setContentType("text/xml");
        i2b2Post.setEntity(xmlEntity);
        HttpResponse resp = http.execute(i2b2Post);

        return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(resp.getEntity().getContent());
    }

    /**
     * Generates a random message number for i2b2 requests. Copied from the i2b2
     * SMART project: https://community.i2b2.org/wiki/display/SMArt/SMART+i2b2
     * in file: edu.harvard.i2b2.smart.ws.SmartAuthHelper
     * 
     * @return a unique message ID
     */
    static String generateMessageId() {
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

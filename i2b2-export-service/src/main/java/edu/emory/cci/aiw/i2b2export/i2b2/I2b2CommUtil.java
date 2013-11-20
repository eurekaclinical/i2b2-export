package edu.emory.cci.aiw.i2b2export.i2b2;

/*
 * #%L
 * i2b2 Export Service
 * %%
 * Copyright (C) 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import edu.emory.cci.aiw.i2b2export.xml.XmlUtil;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Class for utility methods for communicating with i2b2.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public final class I2b2CommUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(I2b2CommUtil.class);

	/*
	 * the default name of the application's properties file
	 */
	private static final String DEFAULT_PROPERTIES_FILE = "i2b2export.properties";

	/*
	 * the default path to the application's properties file
	 */
	private static final String DEFAULT_PROPERTIES_PATH = "/etc/i2b2export/" + DEFAULT_PROPERTIES_FILE;

	/*
	 * the system property for setting the location of the application's properties file
	 */
	private static final String PROPERTIES_FILE_PROPERTY = "i2b2export.propertiesFile";

	/*
	 * the name of the property that defines the URL of the i2b2 proxy cell
	 */
	private static final String I2B2_PROXY_URL_PROPERTY = "i2b2ProxyUrl";

	/*
	 * the name of the property that defines the URL of the i2b2 services
	 */
	private static final String I2B2_SERVICE_HOST_URL_PROPERTY = "i2b2ServiceHostUrl";

	private static String i2b2ProxyUrl = "";
	private static String i2b2ServiceHostUrl = "";

	/*
	 * the location of the project's i2b2 messaging XML templates
	 */
	static final String TEMPLATES_DIR = "i2b2-xml-templates";

	/*
	 * The i2b2 date format. The colon (:) must be stripped out of the timezone portion first.
	 */
	public static final String I2B2_DATE_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	/**
	 * Private constructor to prevent instantiation
	 */
	private I2b2CommUtil() {
	}

	/**
	 * Reads the application's properties from a properties file. It first checks if the system property has overridden
	 * the default location of the properties file. If that is false, it then checks if the properties file exists in
	 * the default location. If that also fails, it then looks in the classpath for the properties files. If all checks
	 * file, it logs an error that the application will not work correctly.
	 */
	private static void readProperties() {
		InputStream propFileStream = null;
		try {
			if (System.getProperties().containsKey(PROPERTIES_FILE_PROPERTY) && new File(System.getProperty(PROPERTIES_FILE_PROPERTY)).exists()) {
				LOGGER.info("Using system property for application properties file: {}", System.getProperty(PROPERTIES_FILE_PROPERTY));
				propFileStream = new FileInputStream(System.getProperty(PROPERTIES_FILE_PROPERTY));
			} else if (new File(DEFAULT_PROPERTIES_PATH).exists()) {
				if (System.getProperties().containsKey(PROPERTIES_FILE_PROPERTY)) {
					LOGGER.warn("Specified properties file not found: {}", System.getProperty(PROPERTIES_FILE_PROPERTY));
				}
				LOGGER.info("Using default location for application properties file: {}", DEFAULT_PROPERTIES_PATH);
				propFileStream = new FileInputStream(DEFAULT_PROPERTIES_PATH);
			} else {
				if (System.getProperties().containsKey(PROPERTIES_FILE_PROPERTY)) {
					LOGGER.warn("Specified properties file not found: {}", System.getProperty(PROPERTIES_FILE_PROPERTY));
					LOGGER.info("Application properties file not found in default location: {}", DEFAULT_PROPERTIES_PATH);
				}
				LOGGER.info("Using classpath for application properties file");
				propFileStream = I2b2CommUtil.class.getResourceAsStream("/" + DEFAULT_PROPERTIES_FILE);

				if (null == propFileStream) {
					LOGGER.warn("Could not find properties file in classpath: {}", DEFAULT_PROPERTIES_FILE);
					LOGGER.error("Could not find properties file in system property {}, default location {}, or classpath!! Service will not work properly!!", PROPERTIES_FILE_PROPERTY, DEFAULT_PROPERTIES_PATH);
				}
			}
			if (null != propFileStream) {
				Properties p = new Properties();
				p.load(propFileStream);
				if (!p.containsKey(I2B2_PROXY_URL_PROPERTY) || !p.containsKey(I2B2_SERVICE_HOST_URL_PROPERTY)) {
					LOGGER.error("Could not find required property in properties file. Service will not work properly!! Make sure the following properties are defined: {}, {}", I2B2_PROXY_URL_PROPERTY, I2B2_SERVICE_HOST_URL_PROPERTY);
				} else {
					i2b2ProxyUrl = p.getProperty(I2B2_PROXY_URL_PROPERTY);
					i2b2ServiceHostUrl = p.getProperty(I2B2_SERVICE_HOST_URL_PROPERTY);
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (null != propFileStream) {
				try {
					propFileStream.close();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Gets the URL of the i2b2 proxy cell. Reads from the properties file first if necessary.
	 *
	 * @return the URL as a String
	 */
	public static String getProxyUrl() {
		if (null == i2b2ProxyUrl || i2b2ProxyUrl.isEmpty()) {
			readProperties();
		}
		return i2b2ProxyUrl;
	}

	/**
	 * Gets the URL of the i2b2 services. Reads from the properties file first if necessary.
	 *
	 * @return the URL as a String
	 */
	public static String getI2b2ServiceHostUrl() {
		if (null == i2b2ServiceHostUrl || i2b2ServiceHostUrl.isEmpty()) {
			readProperties();
		}
		return i2b2ServiceHostUrl;
	}

	/**
	 * Performs an HTTP POST of an XML request to the i2b2 proxy cell. Request XML must contain
	 * the redirect_url node with the address of the actual service endpoint to call
	 * in order for the request to be properly routed.
	 *
	 * @param xml the XML request to send
	 * @return the XML response from i2b2 as a {@link Document}
	 * @throws ClientProtocolException      if an error occurs
	 * @throws IOException                  if an error occurs
	 * @throws ParserConfigurationException if an error occurs
	 * @throws SAXException                 if an error occurs
	 * @throws IllegalStateException        if an error occurs
	 */
	public static Document postXmlToI2b2(String xml) throws ClientProtocolException, IOException, IllegalStateException,
			SAXException, ParserConfigurationException {
		LOGGER.debug("POSTing XML: {}", xml);
		HttpClient http = new DefaultHttpClient();
		HttpPost i2b2Post = new HttpPost(getProxyUrl());
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
			LOGGER.debug("Response XML: {}", respXml.toString());

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

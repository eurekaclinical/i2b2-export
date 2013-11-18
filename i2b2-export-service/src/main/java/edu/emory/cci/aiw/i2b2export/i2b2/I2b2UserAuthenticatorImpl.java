package edu.emory.cci.aiw.i2b2export.i2b2;

/*
 * #%L
 * i2b2 Patient Data Export Service
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

import edu.emory.cci.aiw.i2b2export.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2export.xml.I2b2PatientDataExportServiceXmlException;
import edu.emory.cci.aiw.i2b2export.xml.XmlUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public final class I2b2UserAuthenticatorImpl implements I2b2UserAuthenticator {

	private static final Logger LOGGER = LoggerFactory.getLogger(I2b2UserAuthenticatorImpl.class);

	private final Configuration config;

	/**
	 * Creates a new I2b2UserAuthenticatorImpl instance based on the given XML.
	 */
	public I2b2UserAuthenticatorImpl() {
		this.config = new Configuration();
		this.config.setClassForTemplateLoading(this.getClass(), "/");
		this.config.setObjectWrapper(new DefaultObjectWrapper());
	}

	/**
	 * Authenticates an i2b2 user.
	 *
	 * @return <code>true</code> if the user was authenticated,
	 *         <code>false</code> otherwise
	 * @throws edu.emory.cci.aiw.i2b2export.xml.I2b2PatientDataExportServiceXmlException
	 *          if an error occurred in the parsing of the incoming or
	 *          response XML
	 */
	public boolean authenticateUser(I2b2AuthMetadata authMetadata) throws
			I2b2PatientDataExportServiceXmlException {
		try {
			LOGGER.debug("Attempting to authenticate i2b2 user: {} with password node: {} in domain {} for project {}",
					new String[]{
							authMetadata.getUsername(), authMetadata.getPasswordNode(),
							authMetadata.getDomain(), authMetadata.getProjectId()});

			Template tmpl = this.config.getTemplate(I2b2CommUtil.TEMPLATES_DIR + "/i2b2_user_auth.ftl");
			StringWriter writer = new StringWriter();

			DateFormat sdf = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);
			Date now = new Date();
			String messageId = I2b2CommUtil.generateMessageId();

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("redirectHost", I2b2CommUtil.getI2b2ServiceHostUrl());
			params.put("domain", authMetadata.getDomain());
			params.put("username", authMetadata.getUsername());
			params.put("passwordNode", authMetadata.getPasswordNode());
			params.put("messageId", messageId);
			params.put("messageDatetime", sdf.format(now));
			params.put("projectId", authMetadata.getProjectId());

			tmpl.process(params, writer);
			Document respXml = I2b2CommUtil.postXmlToI2b2(writer.toString());

			String status = (String) XmlUtil.evalXPath(respXml,
					"//response_header/result_status/status/@type",
					XPathConstants.STRING);

			LOGGER.debug("Received authentication status: {}", status);

			return "DONE".equalsIgnoreCase(status);
		} catch (IOException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (XPathExpressionException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (SAXException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (ParserConfigurationException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		} catch (TemplateException e) {
			throw new I2b2PatientDataExportServiceXmlException(e);
		}

	}
}

package edu.emory.bmi.aiw.i2b2export.i2b2;

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

import edu.emory.bmi.aiw.i2b2export.comm.I2b2AuthMetadata;
import edu.emory.bmi.aiw.i2b2export.comm.I2b2PatientSet;
import edu.emory.bmi.aiw.i2b2export.entity.I2b2Concept;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.I2b2PdoResultParser;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.I2b2PdoResults;
import edu.emory.bmi.aiw.i2b2export.xml.I2b2ExportServiceXmlException;
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

/**
 * Implementation of the i2b2 PDO retriever interface. It retrieves the data by filling the PDO request XML template
 * and sending that XML to the i2b2 service as defined the application's properties file (see {@link I2b2CommUtil}).
 *
 * @author Michel Mansour
 * @since 1.0
 */
public final class I2b2PdoRetrieverImpl implements I2b2PdoRetriever {

	private final Configuration config;

	/**
	 * Default no-arg constructor.
	 */
	public I2b2PdoRetrieverImpl() {
		this.config = new Configuration();
		this.config.setClassForTemplateLoading(this.getClass(), "/");
		this.config.setObjectWrapper(new DefaultObjectWrapper());
	}

	@Override
	public I2b2PdoResults retrieve(I2b2AuthMetadata authMetadata,
								Collection<I2b2Concept> concepts,
								I2b2PatientSet patientSet) throws I2b2ExportServiceXmlException {
		try {
			Template tmpl = this.config.getTemplate(I2b2CommUtil.TEMPLATES_DIR + "/i2b2_pdo_request.ftl");
			StringWriter writer = new StringWriter();

			String messageId = I2b2CommUtil.generateMessageId();

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("redirectHost", I2b2CommUtil.getI2b2ServiceHostUrl());
			params.put("domain", authMetadata.getDomain());
			params.put("username", authMetadata.getUsername());
			params.put("passwordNode", authMetadata.getPasswordNode());
			params.put("messageId", messageId);
			params.put("projectId", authMetadata.getProjectId());
			params.put("patientListMax", patientSet.getPatientSetSize());
			params.put("patientListMin", "1");
			params.put("patientSetCollId", patientSet.getPatientSetCollId());
			params.put("items", concepts);

			tmpl.process(params, writer);
			Document respXml = I2b2CommUtil.postXmlToI2b2(writer.toString());
			I2b2PdoResultParser parser = new I2b2PdoResultParser(respXml);
			return parser.parse();
		} catch (IOException e) {
			throw new I2b2ExportServiceXmlException(e);
		} catch (TemplateException e) {
			throw new I2b2ExportServiceXmlException(e);
		} catch (SAXException e) {
			throw new I2b2ExportServiceXmlException(e);
		} catch (ParserConfigurationException e) {
			throw new I2b2ExportServiceXmlException(e);
		}
	}
}

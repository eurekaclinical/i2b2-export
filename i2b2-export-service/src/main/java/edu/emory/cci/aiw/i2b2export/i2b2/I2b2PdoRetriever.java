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
import edu.emory.cci.aiw.i2b2export.comm.I2b2PatientSet;
import edu.emory.cci.aiw.i2b2export.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2export.i2b2.pdo.I2b2PdoResults;
import edu.emory.cci.aiw.i2b2export.xml.I2b2ExportServiceXmlException;

import java.util.Collection;

/**
 * Interface for retrieving PDO results from i2b2.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public interface I2b2PdoRetriever {

	/**
	 * Attempts to retrieve PDO results from i2b2 for the given patient set. The results are filtered based on the
	 * requested concepts, and the request will succeed only if the given user is authorized to make the request.
	 *
	 * @param authMetadata the i2b2 authentication metadata of the user making the request
	 * @param concepts the i2b2 concepts to query for
	 * @param patientSet the i2b2 patient set to retrieve
	 * @return a {@link I2b2PdoResults} object containing the patients, events, observers, and observations retrieved
	 * from i2b2
	 * @throws I2b2ExportServiceXmlException if an error occurs while processing the XML returned by i2b2
	 */
	public I2b2PdoResults retrieve(I2b2AuthMetadata authMetadata,
								Collection<I2b2Concept> concepts,
								I2b2PatientSet patientSet) throws I2b2ExportServiceXmlException;
}

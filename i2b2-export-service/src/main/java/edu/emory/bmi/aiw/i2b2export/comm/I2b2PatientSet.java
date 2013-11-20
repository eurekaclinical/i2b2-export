package edu.emory.bmi.aiw.i2b2export.comm;

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

/**
 * A bean for i2b2 patient sets. Used for retrieving PDO results for formatting from i2b2. Instances of this class will
 * generally be created and populated using JSON objects sent by the client.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public final class I2b2PatientSet {

	/*
	 * the i2b2 patient set collection ID
	 */
	private Integer patientSetCollId;

	/*
	 * the size of the patient set
	 */
	private Integer patientSetSize;

	/**
	 * Gets the i2b2 patient set collection ID.
	 *
	 * @return the patient set collection ID
	 */
	public Integer getPatientSetCollId() {
		return patientSetCollId;
	}

	/**
	 * Sets the i2b2 patient set collection ID
	 *
	 * @param patientSetCollId the patient set collection ID
	 */
	public void setPatientSetCollId(Integer patientSetCollId) {
		this.patientSetCollId = patientSetCollId;
	}

	/**
	 * Gets the patient set size.
	 *
	 * @return the patient set size
	 */
	public Integer getPatientSetSize() {
		return patientSetSize;
	}

	/**
	 * Sets the patient set size
	 *
	 * @param patientSetSize the patient set size
	 */
	public void setPatientSetSize(Integer patientSetSize) {
		this.patientSetSize = patientSetSize;
	}
}

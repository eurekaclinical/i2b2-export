package edu.emory.cci.aiw.i2b2export.comm;

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
 * A bean to communicate output configuration summaries to the client. The summary consists only of the ID and name of
 * the configuration, which is used, for example, in displaying a list of them.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class OutputConfigurationSummary {

	/*
	 * configuration ID
	 */
	private Long configurationId;

	/*
	 * name of the configuration
	 */
	private String configurationName;

	/**
	 * Default constructor. Sets the ID and name to unusable values. Should be called only if the modifiers will be
	 * called immediately after, for instance by a JSON deserializer.
	 */
	public OutputConfigurationSummary() {
		this(-1L, "");
	}

	/**
	 * Constructs an instance with the given configuration ID and name.
	 *
	 * @param id the configuration ID
	 * @param name the configuration name
	 */
	public OutputConfigurationSummary(Long id, String name) {
		this.configurationId = id;
		this.configurationName = name;
	}

	/**
	 * Gets the configuration ID.
	 *
	 * @return the configuration ID
	 */
	public Long getConfigurationId() {
		return configurationId;
	}

	/**
	 * Sets the configuration ID
	 *
	 * @param configurationId the configuration ID
	 */
	public void setConfigurationId(Long configurationId) {
		this.configurationId = configurationId;
	}

	/**
	 * Gets the configuration name.
	 *
	 * @return the configuration name
	 */
	public String getConfigurationName() {
		return configurationName;
	}

	/**
	 * Sets the configuration name.
	 *
	 * @param configurationName the configuration name
	 */
	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}
}

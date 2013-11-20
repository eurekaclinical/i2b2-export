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
 * A bean for storing i2b2 user authentication metadata. Instances of this class will generally be created and
 * populated using JSON objects sent from the client.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public final class I2b2AuthMetadata {

	/*
	 * the user's domain
	 */
	private String domain;

	/*
	 * the user's username
	 */
	private String username;

	/*
	 * the user's password node
	 */
	private String passwordNode;

	/*
	 * the ID of the project the user is accessing
	 */
	private String projectId;

	/**
	 * Gets the user's domain.
	 *
	 * @return the domain as a String
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the user's domain.
	 *
	 * @param domain the domain as a String
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Gets the user's username.
	 *
	 * @return the username as a String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the user's username.
	 *
	 * @param username the username as a String
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the user's password node. This is an entire XML node containing any security information related to the
	 * user's password.
	 *
	 * @return the XML of the password node, as a String
	 */
	public String getPasswordNode() {
		return passwordNode;
	}

	/**
	 * Sets the user's password XML node.
	 *
	 * @param passwordNode the password node as a String
	 */
	public void setPasswordNode(String passwordNode) {
		this.passwordNode = passwordNode;
	}

	/**
	 * Gets the user's project ID.
	 *
	 * @return the project ID as a String
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * Sets the user's project ID
	 *
	 * @param projectId the project ID as a String
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}

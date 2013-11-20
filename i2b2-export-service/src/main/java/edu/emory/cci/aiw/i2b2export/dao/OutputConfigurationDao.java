package edu.emory.cci.aiw.i2b2export.dao;

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

import edu.emory.cci.aiw.i2b2export.entity.OutputConfiguration;

import java.util.List;

/**
 * Data access object for {@link OutputConfiguration}. Defines all of the CRUD
 * operations.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public interface OutputConfigurationDao {

	/**
	 * Retrieves all configurations for the given user (by username).
	 *
	 * @param username the username of the user whose configurations are to be retrieved
	 * @return all of the {@link OutputConfiguration}s for the user, as a list
	 */
	public List<OutputConfiguration> getAllByUsername(String username);

	/**
	 * Retrieves the output configuration associated with the specified ID.
	 *
	 * @param configId the ID of the output configuration to retrieve
	 * @return the {@link OutputConfiguration} having the given ID
	 */
	public OutputConfiguration getById(Long configId);

	/**
	 * Retrieves the output configuration belonging to the given user and having the given name.
	 *
	 * @param username the username of the user
	 * @param configName the name of the configuration
	 * @return the {@link OutputConfiguration} for the given user that has the given name
	 */
	public OutputConfiguration getByUsernameAndConfigName(String username,
														String configName);

	/**
	 * Creates a new output configuration record from the given output configuration.
	 *
	 * @param config the output configuration to create
	 */
	public void create(OutputConfiguration config);

	/**
	 * Updates a given existing configuration record with a new configuration.
	 *
	 * @param existingConfig the existing configuration record to update
	 * @param newConfig the new configuration to update with
	 */
	public void update(OutputConfiguration existingConfig, OutputConfiguration newConfig);

	/**
	 * Deletes the given configuration record.
	 *
	 * @param config the configuration to delete
	 */
	public void delete(OutputConfiguration config);
}

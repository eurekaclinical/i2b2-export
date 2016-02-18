package edu.emory.bmi.aiw.i2b2export.dao;

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

import edu.emory.bmi.aiw.i2b2export.entity.OutputConfigurationEntity;

import java.util.List;

/**
 * Data access object for {@link OutputConfigurationEntity}. Defines all of the CRUD
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
	 * @return all of the {@link OutputConfigurationEntity}s for the user, as a list
	 */
	public List<OutputConfigurationEntity> getAllByUsername(String username);

	/**
	 * Retrieves the output configuration associated with the specified ID.
	 *
	 * @param configId the ID of the output configuration to retrieve
	 * @return the {@link OutputConfigurationEntity} having the given ID
	 */
	public OutputConfigurationEntity getById(Long configId);

	/**
	 * Retrieves the output configuration belonging to the given user and having the given name.
	 *
	 * @param username the username of the user
	 * @param configName the name of the configuration
	 * @return the {@link OutputConfigurationEntity} for the given user that has the given name
	 */
	public OutputConfigurationEntity getByUsernameAndConfigName(String username,
														String configName);

	/**
	 * Creates a new output configuration record from the given output configuration.
	 *
	 * @param config the output configuration to create
	 */
	public void create(OutputConfigurationEntity config);

	/**
	 * Updates a given existing configuration record.
	 *
	 * @param config the existing configuration record to update
	 */
	public void update(OutputConfigurationEntity config);

	/**
	 * Deletes the given configuration record.
	 *
	 * @param config the configuration to remove
	 */
	public void remove(OutputConfigurationEntity config);
	
	OutputConfigurationEntity refresh(OutputConfigurationEntity config);
}

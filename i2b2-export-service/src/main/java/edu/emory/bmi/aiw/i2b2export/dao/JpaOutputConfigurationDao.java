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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfiguration;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfiguration;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfiguration_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA implementation of the output configuration data access object.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class JpaOutputConfigurationDao implements OutputConfigurationDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(JpaOutputConfigurationDao.class);

	private final Provider<EntityManager> provider;

	/*
	 * the column configuration DAO to use to propagate changes to the appropriate column configurations
	 */
	private final OutputColumnConfigurationDao colConfigDao;

	/**
	 * Default constructor.
	 *
	 * @param provider the entity manager provider
	 * @param colConfigDao the column configuration DAO
	 */
	@Inject
	public JpaOutputConfigurationDao(Provider<EntityManager> provider, OutputColumnConfigurationDao colConfigDao) {
		this.provider = provider;
		this.colConfigDao = colConfigDao;
	}

	private EntityManager getEntityManager() {
		return this.provider.get();
	}

	@Override
	public List<OutputConfiguration> getAllByUsername(String username) {
		LOGGER.debug("Retrieving all configurations for user: {}", username);

		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OutputConfiguration> query = builder.createQuery(OutputConfiguration.class);
		Root<OutputConfiguration> root = query.from(OutputConfiguration.class);
		Path<String> usernamePath = root.get(OutputConfiguration_.username);
		CriteriaQuery<OutputConfiguration> where = query.where(builder.and(
				builder.equal(usernamePath, username)));
		TypedQuery<OutputConfiguration> typedQuery = entityManager
				.createQuery(where);

		List<OutputConfiguration> result = typedQuery.getResultList();

		if (LOGGER.isDebugEnabled()) {
			List<String> names = new ArrayList<String>();
			for (OutputConfiguration config : result) {
				names.add(config.getName());
			}
			LOGGER.debug("Retrieved configurations: {}", names);
		}

		return result;
	}

	@Override
	public OutputConfiguration getById(Long configId) {
		LOGGER.debug("Retrieving configuration with id: {}", configId);
		OutputConfiguration config = this.getEntityManager().find(OutputConfiguration.class,
				configId);
		LOGGER.debug("Retrieved configuration: {}", config.getName());
		return config;
	}

	@Override
	public OutputConfiguration getByUsernameAndConfigName(String username,
														String configName) {
		LOGGER.debug("Retrieving configuration for user: {} and with name: {}", username, configName);
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OutputConfiguration> query = builder.createQuery(OutputConfiguration.class);
		Root<OutputConfiguration> root = query.from(OutputConfiguration.class);
		Path<String> usernamePath = root.get(OutputConfiguration_.username);
		Path<String> configNamePath = root.get(OutputConfiguration_.name);
		CriteriaQuery<OutputConfiguration> where = query.where(builder.and(builder.equal(usernamePath, username),
						builder.equal(configNamePath, configName)));
		TypedQuery<OutputConfiguration> typedQuery = entityManager.createQuery(where);

		List<OutputConfiguration> result = typedQuery.getResultList();
		if (result == null || result.isEmpty()) {
			LOGGER.warn("No configuration found for user: {} and with name: {}", username, configName);
			return null;
		} else {
			OutputConfiguration config = result.get(0);
			LOGGER.debug("Found configuration with name: {}", config.getName());
			return config;
		}
	}

	@Override
	@Transactional
	public void create(OutputConfiguration config) {
		LOGGER.debug("Creating configuration for user {} and with name {}", config.getUsername(), config.getName());
		for (OutputColumnConfiguration colConfig : config.getColumnConfigs()) {
			colConfig.setOutputConfig(config);
		}
		this.getEntityManager().persist(config);
		LOGGER.debug("Created configuration with id: {}", config.getId());
	}

	@Override
	@Transactional
	public void update(OutputConfiguration existingConfig, OutputConfiguration newConfig) {
		LOGGER.debug("Updating configuration with id: {}", existingConfig.getId());
		for (OutputColumnConfiguration colConfig : existingConfig.getColumnConfigs()) {
			this.colConfigDao.delete(colConfig);
		}
		existingConfig.setRowDimension(newConfig.getRowDimension());
		existingConfig.setWhitespaceReplacement(newConfig.getWhitespaceReplacement());
		existingConfig.setSeparator(newConfig.getSeparator());
		existingConfig.setMissingValue(newConfig.getMissingValue());
		existingConfig.getColumnConfigs().clear();
		for (OutputColumnConfiguration colConfig : newConfig.getColumnConfigs()) {
			existingConfig.getColumnConfigs().add(colConfig);
			colConfig.setOutputConfig(existingConfig);
		}
		LOGGER.debug("Configuration updated");
	}

	@Override
	@Transactional
	public void delete(OutputConfiguration config) {
		LOGGER.debug("Deleting configuration with id: {}", config.getId());
		this.getEntityManager().remove(config);
		LOGGER.debug("Configuration deleted");
	}
}

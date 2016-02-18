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
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfigurationEntity_;
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

	/**
	 * Default constructor.
	 *
	 * @param provider the entity manager provider
	 */
	@Inject
	public JpaOutputConfigurationDao(Provider<EntityManager> provider) {
		this.provider = provider;
	}

	private EntityManager getEntityManager() {
		return this.provider.get();
	}

	@Override
	public List<OutputConfigurationEntity> getAllByUsername(String username) {
		LOGGER.debug("Retrieving all configurations for user: {}", username);

		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OutputConfigurationEntity> query = builder.createQuery(OutputConfigurationEntity.class);
		Root<OutputConfigurationEntity> root = query.from(OutputConfigurationEntity.class);
		Path<String> usernamePath = root.get(OutputConfigurationEntity_.username);
		CriteriaQuery<OutputConfigurationEntity> where = query.where(builder.and(
				builder.equal(usernamePath, username)));
		TypedQuery<OutputConfigurationEntity> typedQuery = entityManager
				.createQuery(where);

		List<OutputConfigurationEntity> result = typedQuery.getResultList();

		if (LOGGER.isDebugEnabled()) {
			List<String> names = new ArrayList<>();
			for (OutputConfigurationEntity config : result) {
				names.add(config.getName());
			}
			LOGGER.debug("Retrieved configurations: {}", names);
		}

		return result;
	}

	@Override
	public OutputConfigurationEntity getById(Long configId) {
		LOGGER.debug("Retrieving configuration with id: {}", configId);
		OutputConfigurationEntity config = this.getEntityManager().find(OutputConfigurationEntity.class,
				configId);
		LOGGER.debug("Retrieved configuration: {}", config.getName());
		return config;
	}

	@Override
	public OutputConfigurationEntity getByUsernameAndConfigName(String username,
														String configName) {
		LOGGER.debug("Retrieving configuration for user: {} and with name: {}", username, configName);
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OutputConfigurationEntity> query = builder.createQuery(OutputConfigurationEntity.class);
		Root<OutputConfigurationEntity> root = query.from(OutputConfigurationEntity.class);
		Path<String> usernamePath = root.get(OutputConfigurationEntity_.username);
		Path<String> configNamePath = root.get(OutputConfigurationEntity_.name);
		CriteriaQuery<OutputConfigurationEntity> where = query.where(builder.and(builder.equal(usernamePath, username),
						builder.equal(configNamePath, configName)));
		TypedQuery<OutputConfigurationEntity> typedQuery = entityManager.createQuery(where);

		List<OutputConfigurationEntity> result = typedQuery.getResultList();
		if (result == null || result.isEmpty()) {
			LOGGER.warn("No configuration found for user: {} and with name: {}", username, configName);
			return null;
		} else {
			OutputConfigurationEntity config = result.get(0);
			LOGGER.debug("Found configuration with name: {}", config.getName());
			return config;
		}
	}

	@Override
	@Transactional
	public void create(OutputConfigurationEntity config) {
		LOGGER.debug("Creating configuration for user {} and with name {}", config.getUsername(), config.getName());
		this.getEntityManager().persist(config);
		LOGGER.debug("Created configuration with id: {}", config.getId());
	}

	@Override
	@Transactional
	public void update(OutputConfigurationEntity config) {
		LOGGER.debug("Updating configuration with id: {}", config.getId());
		this.getEntityManager().merge(config);
		LOGGER.debug("Configuration updated");
	}

	@Override
	@Transactional
	public void remove(OutputConfigurationEntity config) {
		LOGGER.debug("Deleting configuration with id: {}", config.getId());
		EntityManager entityManager = this.getEntityManager();
		if (entityManager.contains(config)) {
			entityManager.remove(config);
		} else {
			entityManager.remove(entityManager.merge(config));
		}
		LOGGER.debug("Configuration deleted");
	}
	
	@Override
	public OutputConfigurationEntity refresh(OutputConfigurationEntity config) {
		this.getEntityManager().refresh(config);
		return config;
	}
}

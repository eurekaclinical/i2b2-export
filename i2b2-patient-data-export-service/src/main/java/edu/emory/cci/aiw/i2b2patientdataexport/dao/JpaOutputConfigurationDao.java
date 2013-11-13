package edu.emory.cci.aiw.i2b2patientdataexport.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration_;
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

public class JpaOutputConfigurationDao implements OutputConfigurationDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(JpaOutputConfigurationDao.class);

	private final Provider<EntityManager> provider;
	private final OutputColumnConfigurationDao colConfigDao;

	@Inject
	public JpaOutputConfigurationDao(Provider<EntityManager> provider, OutputColumnConfigurationDao colConfigDao) {
		this.provider = provider;
		this.colConfigDao = colConfigDao;
	}

	private EntityManager getEntityManager() {
		return this.provider.get();
	}

	public List<OutputConfiguration> getAllByUsername(String username) {
		LOGGER.debug("Retrieving all configurations for user: {}", username);

		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OutputConfiguration> query = builder.createQuery
				(OutputConfiguration.class);
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

	public OutputConfiguration getById(Long configId) {
		LOGGER.debug("Retrieving configuration with id: {}", configId);
		OutputConfiguration config = this.getEntityManager().find(OutputConfiguration.class,
				configId);
		LOGGER.debug("Retrieved configuration: {}", config.getName());
		return config;
	}

	public OutputConfiguration getByUsernameAndConfigName(String username,
														  String configName) {
		LOGGER.debug("Retrieving configuration for user: {} and with name: {}", username, configName);
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OutputConfiguration> query = builder.createQuery
				(OutputConfiguration.class);
		Root<OutputConfiguration> root = query.from(OutputConfiguration.class);
		Path<String> usernamePath = root.get(OutputConfiguration_.username);
		Path<String> configNamePath = root.get(OutputConfiguration_.name);
		CriteriaQuery<OutputConfiguration> where = query.where(builder.and
				(builder.equal(usernamePath, username),
						builder.equal(configNamePath, configName)));
		TypedQuery<OutputConfiguration> typedQuery = entityManager
				.createQuery(where);

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

	@Transactional
	public void create(OutputConfiguration config) {
		LOGGER.debug("Creating configuration for user {} and with name {}", config.getUsername(), config.getName());
		this.getEntityManager().persist(config);
		LOGGER.debug("Created configuration with id: {}", config.getId());
	}

	@Transactional
	public void update(OutputConfiguration oldConfig, OutputConfiguration newConfig) {
		LOGGER.debug("Updating configuration with id: {}", oldConfig.getId());
		for (OutputColumnConfiguration colConfig : oldConfig.getColumnConfigs()) {
			this.colConfigDao.delete(colConfig);
		}
		oldConfig.setRowDimension(newConfig.getRowDimension());
		oldConfig.setWhitespaceReplacement(newConfig.getWhitespaceReplacement());
		oldConfig.setSeparator(newConfig.getSeparator());
		oldConfig.setMissingValue(newConfig.getMissingValue());
		oldConfig.getColumnConfigs().clear();
		for (OutputColumnConfiguration colConfig : newConfig.getColumnConfigs()) {
			oldConfig.getColumnConfigs().add(colConfig);
		}
		LOGGER.debug("Configuration updated");
	}

	@Transactional
	public void delete(OutputConfiguration config) {
		LOGGER.debug("Deleting configuration with id: {}", config.getId());
		this.getEntityManager().remove(config);
		LOGGER.debug("Configuration deleted");
	}
}

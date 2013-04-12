package edu.emory.cci.aiw.i2b2patientdataexport.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration_;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpaOutputConfigurationDao implements OutputConfigurationDao {

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
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OutputConfiguration> query = builder.createQuery
				(OutputConfiguration.class);
		Root<OutputConfiguration> root = query.from(OutputConfiguration.class);
		Path<String> usernamePath = root.get(OutputConfiguration_.username);
		Path<Boolean> isTempPath = root.get(OutputConfiguration_.isTemporary);
		CriteriaQuery<OutputConfiguration> where = query.where(builder.and(
				builder.equal(usernamePath, username), builder.equal(isTempPath,
				Boolean.FALSE)));
		TypedQuery<OutputConfiguration> typedQuery = entityManager
				.createQuery(where);

		return typedQuery.getResultList();
	}

	public OutputConfiguration getById(Long configId) {
		return this.getEntityManager().find(OutputConfiguration.class,
				configId);
	}

	public OutputConfiguration getByUsernameAndConfigName(String username,
														  String configName) {
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
			return null;
		} else {
			return result.get(0);
		}
	}

	@Transactional
	public void create(OutputConfiguration config) {
        this.getEntityManager().persist(config);
	}

    @Transactional
    public void update(OutputConfiguration oldConfig, OutputConfiguration newConfig) {
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
    }

	@Transactional
	public void delete(OutputConfiguration config) {
		this.getEntityManager().remove(config);
	}
}

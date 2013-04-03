package edu.emory.cci.aiw.i2b2datadownloader.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration_;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpaOutputConfigurationDao implements OutputConfigurationDao {

	private final Provider<EntityManager> provider;

	@Inject
	public JpaOutputConfigurationDao(Provider<EntityManager> provider) {
		this.provider = provider;
	}

	private EntityManager getEntityManager() {
		return this.provider.get();
	}

	public List<OutputConfiguration> getAll(Long userId) {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OutputConfiguration> query = builder.createQuery
				(OutputConfiguration.class);
		Root<OutputConfiguration> root = query.from(OutputConfiguration.class);
		Path<Long> userIdPath = root.get(OutputConfiguration_.userId);
		CriteriaQuery<OutputConfiguration> where = query.where(
				builder.equal(userIdPath, userId));
		TypedQuery<OutputConfiguration> typedQuery = entityManager
				.createQuery(where);

		return typedQuery.getResultList();
	}

	public OutputConfiguration get(Long userId, String name) {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OutputConfiguration> query = builder.createQuery
				(OutputConfiguration.class);
		Root<OutputConfiguration> root = query.from(OutputConfiguration.class);
		Path<Long> userIdPath = root.get(OutputConfiguration_.userId);
		Path<String> namePath = root.get(OutputConfiguration_.name);
		CriteriaQuery<OutputConfiguration> where = query.where(builder.and(builder.equal(userIdPath,
				userId), builder.equal(namePath, name)));
		TypedQuery<OutputConfiguration> typedQuery = entityManager
				.createQuery(where);

		return typedQuery.getSingleResult();
	}
}

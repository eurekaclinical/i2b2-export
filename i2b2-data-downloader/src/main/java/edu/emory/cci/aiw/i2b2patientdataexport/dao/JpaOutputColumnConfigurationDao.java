package edu.emory.cci.aiw.i2b2patientdataexport.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;

import javax.persistence.EntityManager;

public class JpaOutputColumnConfigurationDao implements OutputColumnConfigurationDao {

    private final Provider<EntityManager> provider;

    @Inject
    public JpaOutputColumnConfigurationDao(Provider<EntityManager> provider) {
        this.provider = provider;
    }

    private EntityManager getEntityManager() {
        return this.provider.get();
    }

    @Override
    public void delete(OutputColumnConfiguration colConfig) {
        this.getEntityManager().remove(colConfig);
    }
}

package edu.emory.cci.aiw.i2b2patientdataexport.dao;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;

import java.util.List;

public interface OutputConfigurationDao {

	public List<OutputConfiguration> getAllByUsername(String username);

	public OutputConfiguration getById(Long configId);

	public OutputConfiguration getByUsernameAndConfigName(String username,
														  String configName);

	public void create(OutputConfiguration config);

    public void update(OutputConfiguration oldConfig, OutputConfiguration newConfig);

	public void delete(OutputConfiguration config);

	public void deleteAllTemporaryForUser(String username);
}

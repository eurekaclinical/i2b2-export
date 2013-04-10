package edu.emory.cci.aiw.i2b2datadownloader.dao;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;

import java.util.List;

public interface OutputConfigurationDao {

	public List<OutputConfiguration> getAllByUsername(String username);

	public OutputConfiguration getById(Long configId);

	public OutputConfiguration getByUsernameAndConfigName(String username,
														  String configName);

	public void create(OutputConfiguration config);

    public void update(OutputConfiguration oldConfig, OutputConfiguration newConfig);

	public void delete(OutputConfiguration config);
}

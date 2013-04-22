package edu.emory.cci.aiw.i2b2patientdataexport.dao;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MockOutputConfigurationDao implements OutputConfigurationDao {

	@Override
	public List<OutputConfiguration> getAllByUsername(String username) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public OutputConfiguration getById(Long configId) {
		if (configId < 0) {
			return null;
		} else {
			OutputConfiguration config = new OutputConfiguration();
			config.setId(configId);
			config.setUsername("test-user");
			config.setName("config" + configId);
			config.setRowDimension(OutputConfiguration.RowDimension.PATIENT);
			config.setSeparator(",");
			config.setQuoteChar("\"");
			config.setWhitespaceReplacement("_");
			config.setMissingValue("(null)");
			config.setColumnConfigs(new ArrayList<OutputColumnConfiguration>());

			return config;
		}
	}

	@Override
	public OutputConfiguration getByUsernameAndConfigName(String username, String configName) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void create(OutputConfiguration config) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void update(OutputConfiguration oldConfig, OutputConfiguration newConfig) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void delete(OutputConfiguration config) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}

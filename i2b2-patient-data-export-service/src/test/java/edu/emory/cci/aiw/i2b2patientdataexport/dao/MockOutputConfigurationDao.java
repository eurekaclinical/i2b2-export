package edu.emory.cci.aiw.i2b2patientdataexport.dao;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockOutputConfigurationDao implements OutputConfigurationDao {

	private Map<Integer, OutputConfiguration> configs = new HashMap<Integer,
			OutputConfiguration>();

	@Override
	public List<OutputConfiguration> getAllByUsername(String username) {
		if (username.equals("bad-user")) {
			return null;
		} else {
			List<OutputConfiguration> result = new
					ArrayList<OutputConfiguration>();
			OutputConfiguration config1 = new OutputConfiguration();
			config1.setId(1L);
			config1.setUsername(username);
			config1.setName("config" + 1L);
			config1.setRowDimension(OutputConfiguration.RowDimension.PATIENT);
			config1.setSeparator(",");
			config1.setQuoteChar("\"");
			config1.setWhitespaceReplacement("_");
			config1.setMissingValue("(null)");
			config1.setColumnConfigs(new ArrayList<OutputColumnConfiguration>());

			result.add(config1);

			OutputConfiguration config2 = new OutputConfiguration();
			config2.setId(1L);
			config2.setUsername("test-mismatch-user");
			config2.setName("config" + 1L);
			config2.setRowDimension(OutputConfiguration.RowDimension.PATIENT);
			config2.setSeparator(",");
			config2.setQuoteChar("\"");
			config2.setWhitespaceReplacement("_");
			config2.setMissingValue("(null)");
			config2.setColumnConfigs(new ArrayList<OutputColumnConfiguration>());

			result.add(config2);

			return result;
		}
	}

	@Override
	public OutputConfiguration getById(Long configId) {
		if (configId < 0) {
			return null;
		} else if (configId == 0) {
			OutputConfiguration config = new OutputConfiguration();
			config.setId(0L);
			config.setUsername("test-mismatch-user");
			config.setName("config0");
			config.setRowDimension(OutputConfiguration.RowDimension.PATIENT);
			config.setSeparator(",");
			config.setQuoteChar("\"");
			config.setWhitespaceReplacement("_");
			config.setMissingValue("(null)");
			config.setColumnConfigs(new ArrayList<OutputColumnConfiguration>());

			return config;
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

	}
}

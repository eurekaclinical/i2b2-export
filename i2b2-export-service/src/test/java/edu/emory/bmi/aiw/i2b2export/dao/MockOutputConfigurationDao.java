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

import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.RowDimension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockOutputConfigurationDao implements OutputConfigurationDao {

	private Map<Integer, OutputConfigurationEntity> configs = new HashMap<>();

	@Override
	public List<OutputConfigurationEntity> getAllByUsername(String username) {
		if (username.equals("bad-user")) {
			return null;
		} else {
			List<OutputConfigurationEntity> result = new
					ArrayList<>();
			OutputConfigurationEntity config1 = new OutputConfigurationEntity();
			config1.setId(1L);
			config1.setUsername(username);
			config1.setName("config" + 1L);
			config1.setRowDimension(RowDimension.PATIENT);
			config1.setSeparator(",");
			config1.setQuoteChar("\"");
			config1.setWhitespaceReplacement("_");
			config1.setMissingValue("(null)");
			config1.setColumnConfigs(new ArrayList<>());

			result.add(config1);

			OutputConfigurationEntity config2 = new OutputConfigurationEntity();
			config2.setId(1L);
			config2.setUsername("test-mismatch-user");
			config2.setName("config" + 1L);
			config2.setRowDimension(RowDimension.PATIENT);
			config2.setSeparator(",");
			config2.setQuoteChar("\"");
			config2.setWhitespaceReplacement("_");
			config2.setMissingValue("(null)");
			config2.setColumnConfigs(new ArrayList<>());

			result.add(config2);

			return result;
		}
	}

	@Override
	public OutputConfigurationEntity getById(Long configId) {
		if (configId < 0) {
			return null;
		} else if (configId == 0) {
			OutputConfigurationEntity config = new OutputConfigurationEntity();
			config.setId(0L);
			config.setUsername("test-mismatch-user");
			config.setName("config0");
			config.setRowDimension(RowDimension.PATIENT);
			config.setSeparator(",");
			config.setQuoteChar("\"");
			config.setWhitespaceReplacement("_");
			config.setMissingValue("(null)");
			config.setColumnConfigs(new ArrayList<>());

			return config;
		} else {
			OutputConfigurationEntity config = new OutputConfigurationEntity();
			config.setId(configId);
			config.setUsername("test-user");
			config.setName("config" + configId);
			config.setRowDimension(RowDimension.PATIENT);
			config.setSeparator(",");
			config.setQuoteChar("\"");
			config.setWhitespaceReplacement("_");
			config.setMissingValue("(null)");
			config.setColumnConfigs(new ArrayList<>());

			return config;
		}
	}

	@Override
	public OutputConfigurationEntity getByUsernameAndConfigName(String username, String configName) {
		if (username.equals("test-user") && configName.equals("test-config")) {
			OutputConfigurationEntity config = new OutputConfigurationEntity();
			config.setUsername("test-user");
			config.setName("test-config");

			return config;
		} else if (configName.equals("bad-config")) {
			OutputConfigurationEntity config = new OutputConfigurationEntity();
			config.setUsername("bad-user");
			config.setName("bad-config");

			return config;
		}
		return null;
	}

	@Override
	public void create(OutputConfigurationEntity config) {

	}

	@Override
	public void update(OutputConfigurationEntity config) {

	}

	@Override
	public void remove(OutputConfigurationEntity config) {

	}

	@Override
	public OutputConfigurationEntity refresh(OutputConfigurationEntity config) {
		return config;
	}
	
}

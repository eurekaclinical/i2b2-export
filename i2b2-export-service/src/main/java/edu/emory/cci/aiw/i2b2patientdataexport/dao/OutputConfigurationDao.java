package edu.emory.cci.aiw.i2b2patientdataexport.dao;

/*
 * #%L
 * i2b2 Patient Data Export Service
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
}

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

import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfiguration;

/**
 * Data access object for {@link edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfiguration}.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public interface OutputColumnConfigurationDao {

	/**
	 * Deletes the given column configuration.
	 *
	 * @param colConfig the {@link OutputColumnConfiguration} to delete.
	 */
	public void delete(OutputColumnConfiguration colConfig);
}

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

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfiguration;

import javax.persistence.EntityManager;

/**
 * JPA implementation of the output column configuration data access object.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class JpaOutputColumnConfigurationDao implements OutputColumnConfigurationDao {

	private final Provider<EntityManager> provider;

	/**
	 * Default constructor.
	 *
	 * @param provider the entity manager provider
	 */
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

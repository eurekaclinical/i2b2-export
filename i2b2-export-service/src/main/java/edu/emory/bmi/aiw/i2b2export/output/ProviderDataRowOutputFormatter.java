package edu.emory.bmi.aiw.i2b2export.output;

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

import edu.emory.bmi.aiw.i2b2export.entity.I2b2ConceptEntity;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfigurationEntity;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.arp.javautil.collections.Collections;
import org.eurekaclinical.i2b2.client.pdo.Observation;
import org.eurekaclinical.i2b2.client.pdo.Observer;

/**
 * Output formatter for a provider row.
 *
 * @author Michel Mansour
 * @since 1.0
 */
final class ProviderDataRowOutputFormatter extends DataRowOutputFormatter {
	private final Observer provider;
	private final Map<String, List<Observation>> keyToObx;

	ProviderDataRowOutputFormatter(OutputConfigurationEntity config, Observer provider, Connection con) {
		super(con, config);
		this.provider = provider;
		this.keyToObx = new HashMap<>();
		
		for (Observation o : provider.getObservations()) {
			Collections.putList(keyToObx, o.getConceptPath(), o);
		}
	}

	@Override
	protected int rowPrefix(BufferedWriter writer) throws IOException {
		write(provider.getName(), writer, 0);
		return 1;
	}

	@Override
	protected Collection<Observation> matchingObservations(I2b2ConceptEntity i2b2Concept) {
		List<Observation> get = this.keyToObx.get(i2b2Concept.getI2b2Key());
		if (get != null) {
			return java.util.Collections.unmodifiableCollection(get);
		} else {
			return java.util.Collections.emptyList();
		}
	}
}

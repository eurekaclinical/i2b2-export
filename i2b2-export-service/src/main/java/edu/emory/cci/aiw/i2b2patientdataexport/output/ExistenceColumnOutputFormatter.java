package edu.emory.cci.aiw.i2b2patientdataexport.output;

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

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Observation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public final class ExistenceColumnOutputFormatter extends
		AbstractColumnOutputFormatter {

	public ExistenceColumnOutputFormatter(OutputColumnConfiguration columnConfig, FormatOptions formatOptions) {
		super(columnConfig, formatOptions);
	}

	@Override
	public List<String> format(Collection<Observation> data) {
		if (null == data || data.isEmpty()) {
			return Collections.singletonList("false");
		} else {
			return Collections.singletonList("true");
		}
	}
}

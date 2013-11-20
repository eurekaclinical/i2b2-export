package edu.emory.cci.aiw.i2b2export.output;

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

import edu.emory.cci.aiw.i2b2export.i2b2.pdo.Observation;

import java.util.Collection;
import java.util.List;

/**
 * Interface for formatting columns for output. It defines one method, <code>format</code>, that accepts a collection
 * of {@link Observation}s to be output.
 *
 * @author Michel Mansour
 * @since 1.0
 */
interface ColumnOutputFormatter {

	/**
	 * Generates output for patient data based on a column configuration.
	 *
	 * @param data The observations to format. This collection should include all of the observations matching a particular i2b2 concept.
	 * @return The formatted output
	 */
	List<String> format(Collection<Observation> data);
}

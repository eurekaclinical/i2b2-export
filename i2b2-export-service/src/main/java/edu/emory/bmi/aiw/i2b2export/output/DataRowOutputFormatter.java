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

import edu.emory.bmi.aiw.i2b2export.entity.I2b2Concept;
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfiguration;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfiguration;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Observation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Abstract class for formatting a row of output. Depending on what a row represents (patient, visit, or provider),
 * the first columns of the row will differ, as well as which observations should be displayed. Methods for computing
 * those values are defined as abstract in this class. However, the actual formatting of the row is the same across
 * all implementations.
 *
 * @author Michel Mansour
 * @since 1.0
 */
abstract class DataRowOutputFormatter {

	private final OutputConfiguration config;
	private final FormatOptions formatOptions;

	DataRowOutputFormatter(OutputConfiguration config) {
		this.config = config;
		this.formatOptions = new FormatOptions(config);
	}

	final OutputConfiguration getConfig() {
		return config;
	}

	final FormatOptions getFormatOptions() {
		return formatOptions;
	}

	/**
	 * Finds the observations that match the given i2b2 concept path.
	 *
	 * @param i2b2Concept the i2b2 concept to match
	 * @return a {@link Collection} of {@link Observation}s that match the given concept path
	 */
	abstract Collection<Observation> matchingObservations(I2b2Concept i2b2Concept);

	/**
	 * Generates the first fields of the row that depend on the row dimension rather than the data,
	 * for example, patient id, visit id, etc. The return value is expected to be already joined
	 * by the column separator specified by the output configuration.
	 *
	 * @return the first fields of the row, as a {@link String}, joined by the column separator specified in the
	 *         output configuration
	 */
	abstract List<String> rowPrefix();

	/**
	 * Formats a row of data according to the instance's column configurations and format options. The result is
	 * returned as an array of strings that can be joined together later with the correct delimiter.
	 *
	 * @return an array of {@link String}s representing a single row of output
	 */
	final String[] format() {
		List<String> result = new ArrayList<>();

		result.addAll(rowPrefix());
		for (OutputColumnConfiguration colConfig : getConfig().getColumnConfigs()) {
			Collection<Observation> obxs = matchingObservations(colConfig
					.getI2b2Concept());
			switch (colConfig.getDisplayFormat()) {
				case EXISTENCE:
					result.addAll(new ExistenceColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs));
					break;
				case VALUE:
					result.addAll(new ValueColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs));
					break;
				case AGGREGATION:
					result.addAll(new AggregationColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs));
					break;
				default:
					throw new RuntimeException("display format not provided");
			}
		}

		return result.toArray(new String[result.size()]);
	}
}

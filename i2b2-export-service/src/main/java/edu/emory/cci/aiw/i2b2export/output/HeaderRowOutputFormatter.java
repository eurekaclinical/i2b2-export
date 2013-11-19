package edu.emory.cci.aiw.i2b2export.output;

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

import edu.emory.cci.aiw.i2b2export.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2export.entity.OutputConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Formatter for the header row of an entire result set.
 *
 * @author Michel Mansour
 */
public final class HeaderRowOutputFormatter {
	private final OutputConfiguration outputConfiguration;

	/**
	 * Default constructor. Accepts the output configuration to use to format the header.
	 *
	 * @param outputConfiguration the {@link OutputConfiguration} to use to format the header
	 */
	public HeaderRowOutputFormatter(OutputConfiguration outputConfiguration) {
		this.outputConfiguration = outputConfiguration;
	}

	/**
	 * Formats the header row according to the instance's output configuration. The row is returned as an array
	 * of strings that will be joined later using the correct delimiter.
	 *
	 * @return an array of <code>String</code>s representing the fields of the header row
	 */
	public String[] formatHeader() {
		List<String> result = new ArrayList<>();
		switch (outputConfiguration.getRowDimension()) {
			case PROVIDER:
				result.add("Provider_name");
				break;
			case VISIT:
				result.add("Visit_id");
				result.add("Visit_start");
				result.add("Visit_end");
				// fall through
			case PATIENT:
				result.add(0, "Patient_id"); // patient ID always goes first
				break;
			default:
				throw new RuntimeException("row dimension not provided: user:" +
						" " + outputConfiguration.getUsername() + ", " +
						"name: " + outputConfiguration.getName());
		}
		Collections.sort(outputConfiguration.getColumnConfigs());
		for (int i = 0; i < outputConfiguration.getColumnConfigs().size(); i++) {
			OutputColumnConfiguration colConfig = outputConfiguration
					.getColumnConfigs().get(i);
			String baseColName = colConfig.getColumnName();
			if (outputConfiguration.getWhitespaceReplacement() != null && !outputConfiguration.getWhitespaceReplacement().isEmpty()) {
				baseColName = colConfig.getColumnName().replaceAll("\\s",
						outputConfiguration.getWhitespaceReplacement());
			}
			switch (colConfig.getDisplayFormat()) {
				case EXISTENCE:
					result.add(baseColName);
					break;
				case VALUE:
					for (int j = 0; j < colConfig.getHowMany(); j++) {
						result.add(baseColName + "_value");
						if (colConfig.getIncludeUnits()) {
							result.add(baseColName + "_units");
						}
						if (colConfig.getIncludeTimeRange()) {
							result.add(baseColName + "_start");
							result.add(baseColName + "_end");
						}
					}
					break;
				case AGGREGATION:
					switch (colConfig.getAggregation()) {
						case MIN:
							result.add(baseColName + "_min");
							break;
						case MAX:
							result.add(baseColName + "_max");
							break;
						case AVG:
							result.add(baseColName + "_avg");
							break;
						default:
							continue;
					}
					if (colConfig.getIncludeUnits()) {
						result.add(baseColName + "_units");
					}
					break;
				default:
					break;
			}
		}
		return result.toArray(new String[result.size()]);
	}
}

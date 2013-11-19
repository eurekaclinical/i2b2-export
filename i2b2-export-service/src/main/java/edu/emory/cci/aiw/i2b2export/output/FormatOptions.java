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

import edu.emory.cci.aiw.i2b2export.entity.OutputConfiguration;

/**
 * Container for the formatting options that apply to the entire output configuration but that must be applied at
 * the individual column level. Currently, this only includes the string to use when data is missing for the column.
 *
 * @author Michel Mansour
 */
public final class FormatOptions {
	private final String missingData;

	/**
	 * Default constructor. Accepts an output configuration containing the formatting options.
	 *
	 * @param config the output configuration
	 */
	public FormatOptions(OutputConfiguration config) {
		this.missingData = config.getMissingValue();
	}

	/**
	 * Returns the string to be used in cases where data is not available for a column.
	 *
	 * @return the string to use for missing data
	 */
	public String getMissingData() {
		return missingData;
	}

}

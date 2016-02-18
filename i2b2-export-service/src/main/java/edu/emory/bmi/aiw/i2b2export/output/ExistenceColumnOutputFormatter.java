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

import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Observation;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.Collection;

/**
 * Column formatter for existence columns. Displays true or false depending on whether the data to be formatted is
 * empty or not.
 *
 * @author Michel Mansour
 * @since 1.0
 */
final class ExistenceColumnOutputFormatter extends
		AbstractColumnOutputFormatter {

	/**
	 * Default constructor. Requires the configuration for this column as well as the format options for the entire
	 * output configuration.
	 *
	 * @param columnConfig the configuration of the column to format
	 * @param formatOptions the global format options to apply to the column
	 */
	ExistenceColumnOutputFormatter(OutputColumnConfigurationEntity columnConfig, FormatOptions formatOptions) {
		super(columnConfig, formatOptions);
	}

	@Override
	public int format(Collection<Observation> data, BufferedWriter writer, int colNum) throws IOException {
		if (null == data || data.isEmpty()) {
			write("false", writer, colNum++);
		} else {
			write("true", writer, colNum++);
		}
		return colNum;
	}
}

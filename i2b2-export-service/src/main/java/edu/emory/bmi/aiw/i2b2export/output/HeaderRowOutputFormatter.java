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
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfiguration;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfiguration;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.Collections;
import org.apache.commons.io.IOUtils;

/**
 * Formatter for the header row of an entire result set.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public final class HeaderRowOutputFormatter extends AbstractFormatter implements RowOutputFormatter {

	private final OutputConfiguration outputConfiguration;

	/**
	 * Default constructor. Accepts the output configuration to use to format
	 * the header.
	 *
	 * @param outputConfiguration the {@link OutputConfiguration} to use to
	 * format the header
	 */
	public HeaderRowOutputFormatter(OutputConfiguration outputConfiguration) {
		super(outputConfiguration);
		this.outputConfiguration = outputConfiguration;
	}

	/**
	 * Formats the header row according to the instance's output configuration.
	 * The row is returned as an array of strings that will be joined later
	 * using the correct delimiter.
	 *
	 * @param writer the stream to which the results will go.
	 * @throws java.io.IOException if an error occurred writing the results.
	 */
	@Override
	public void format(BufferedWriter writer) throws IOException {
		int colNum = 0;
		switch (outputConfiguration.getRowDimension()) {
			case PROVIDER:
				write("Provider_name", writer, colNum++);
				break;
			case VISIT:
				write("Patient_id", writer, colNum++);
				write("Visit_id", writer, colNum++);
				write("Visit_start", writer, colNum++);
				write("Visit_end", writer, colNum++);
				break;
			case PATIENT:
				write("Patient_id", writer, colNum++);
				break;
			default:
				throw new RuntimeException("row dimension not provided: user:"
						+ " " + outputConfiguration.getUsername() + ", "
						+ "name: " + outputConfiguration.getName());
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
					write(baseColName, writer, colNum++);
					break;
				case VALUE:
					for (int j = 0; j < colConfig.getHowMany(); j++) {
						write(baseColName + "_value", writer, colNum++);
						if (colConfig.getIncludeUnits()) {
							write(baseColName + "_units", writer, colNum++);
						}
						if (colConfig.getIncludeTimeRange()) {
							write(baseColName + "_start", writer, colNum++);
							write(baseColName + "_end", writer, colNum++);
						}
					}
					break;
				case AGGREGATION:
					switch (colConfig.getAggregation()) {
						case MIN:
							write(baseColName + "_min", writer, colNum++);
							break;
						case MAX:
							write(baseColName + "_max", writer, colNum++);
							break;
						case AVG:
							write(baseColName + "_avg", writer, colNum++);
							break;
						default:
							continue;
					}
					if (colConfig.getIncludeUnits()) {
						write(baseColName + "_units", writer, colNum++);
					}
					break;
				default:
					break;
			}
		}
		writer.write(IOUtils.LINE_SEPARATOR);
	}

}

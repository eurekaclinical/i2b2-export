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

import edu.emory.bmi.aiw.i2b2export.entity.OutputConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.I2b2PdoResults;
import java.io.BufferedWriter;
import java.io.IOException;


/**
 * Output formatter for an entire i2b2 result set.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public final class DataOutputFormatter extends AbstractFormatter implements RowOutputFormatter {

	/*
	 * the i2b2 result set
	 */
	private final I2b2PdoResults pdoResults;

	/**
	 * Default constructor. Sets the output configuration and result set to the given parameters.
	 *
	 * @param config the output configuration to use format the result set for output
	 * @param pdoResults the i2b2 result set to format
	 */
	public DataOutputFormatter(OutputConfigurationEntity config, I2b2PdoResults pdoResults) {
		super(config);
		this.pdoResults = pdoResults;
	}

	/**
	 * Returns the instance's i2b2 results formatted according to the instance's output configuration.
	 *
	 * @param writer the stream to which the formatted results will go.
	 * @throws java.io.IOException if an error occurred writing the results.
	 */
	@Override
	public void format(BufferedWriter writer) throws IOException {
		OutputConfigurationEntity config = getOutputConfiguration();
		new HeaderRowOutputFormatter(config).format(writer);
		switch (config.getRowDimension()) {
			case PATIENT:
				new PatientDataOutputFormatter(config, pdoResults.getPatients()).format(writer);
				break;
			case VISIT:
				new VisitDataOutputFormatter(config, pdoResults.getPatients()).format(writer);
				break;
			case PROVIDER:
				new ProviderDataOutputFormatter(config, pdoResults.getObservers()).format(writer);
				break;
			default:
				throw new AssertionError("no row dimension provided");
		}

	}
}

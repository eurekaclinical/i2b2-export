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

import edu.emory.bmi.aiw.i2b2export.entity.OutputConfiguration;
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
	 * the output configuration to use to format the output
	 */
	private final OutputConfiguration config;

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
	public DataOutputFormatter(OutputConfiguration config, I2b2PdoResults pdoResults) {
		super(config);
		this.config = config;
		this.pdoResults = pdoResults;
	}

	/**
	 * Returns the instance's i2b2 results formatted according to the instance's output configuration.
	 *
	 * @param writer
	 * @return the formatted results as a {@link String}
	 * @throws java.io.IOException
	 */
	@Override
	public void format(BufferedWriter writer) throws IOException {
		new HeaderRowOutputFormatter(config).format(writer);
		switch (this.config.getRowDimension()) {
			case PATIENT:
				new PatientDataOutputFormatter(this.config, pdoResults.getPatients()).format(writer);
				break;
			case VISIT:
				new VisitDataOutputFormatter(this.config, pdoResults.getPatients()).format(writer);
				break;
			case PROVIDER:
				new ProviderDataOutputFormatter(this.config, pdoResults.getObservers()).format(writer);
				break;
			default:
				throw new AssertionError("no row dimension provided");
		}

	}
}

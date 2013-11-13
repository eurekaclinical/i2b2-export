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

import au.com.bytecode.opencsv.CSVWriter;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.I2b2PdoResults;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public final class DataOutputFormatter {

	private final OutputConfiguration config;
	private final I2b2PdoResults pdoResults;

	public DataOutputFormatter(OutputConfiguration config, I2b2PdoResults pdoResults) {
		this.config = config;
		this.pdoResults = pdoResults;
	}

	public String format() {
		List<String[]> result = new ArrayList<String[]>();
		result.add(new HeaderRowOutputFormatter(config).formatHeader());
		switch (this.config.getRowDimension()) {
			case PATIENT:
				result.addAll(new PatientDataOutputFormatter(this.config, pdoResults.getPatients()).format());
				break;
			case VISIT:
				result.addAll(new VisitDataOutputFormatter(this.config, pdoResults.getPatients()).format());
				break;
			case PROVIDER:
				result.addAll(new ProviderDataOutputFormatter(this.config, pdoResults.getObservers()).format());
				break;
			default:
				throw new AssertionError("no row dimension provided");
		}

		StringWriter resultStr = new StringWriter();
		CSVWriter writer;
		if (null == config.getQuoteChar() || config.getQuoteChar().isEmpty()) {
			writer = new CSVWriter(resultStr, config.getSeparator().charAt(0));
		} else {
			writer = new CSVWriter(resultStr, config.getSeparator().charAt(0), config.getQuoteChar().charAt(0));
		}
		writer.writeAll(result);

		return resultStr.toString();
	}
}

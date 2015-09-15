package edu.emory.bmi.aiw.i2b2export.output;

/*
 * #%L
 * i2b2 Export Service
 * %%
 * Copyright (C) 2013 - 2015 Emory University
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
import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author Andrew Post
 */
public abstract class AbstractFormatter {
	private final OutputConfiguration outputConfiguration;

	/**
	 * Default constructor. Accepts the output configuration to use to format
	 * the header.
	 *
	 * @param outputConfiguration the {@link OutputConfiguration} to use to
	 * format the header
	 */
	AbstractFormatter(OutputConfiguration outputConfiguration) {
		this.outputConfiguration = outputConfiguration;
	}
	
	protected void write(String str, BufferedWriter writer, int colNum) throws IOException {
		if (colNum > 0) {
			writer.write(this.outputConfiguration.getSeparator());
		}
		String quoteChar = this.outputConfiguration.getQuoteChar();
		boolean doQuote = quoteChar != null && !quoteChar.isEmpty();
		if (doQuote) {
			writer.write(quoteChar);
		}
		writer.write(str);
		if (doQuote) {
			writer.write(quoteChar);
		}
	}
}

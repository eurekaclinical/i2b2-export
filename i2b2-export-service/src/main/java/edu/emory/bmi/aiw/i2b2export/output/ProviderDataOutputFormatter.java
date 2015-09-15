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
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Observer;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.Collection;
import org.apache.commons.io.IOUtils;

/**
 * Output formatter for when each row represents a provider.
 *
 * @author Michel Mansour
 * @since 1.0
 */
final class ProviderDataOutputFormatter {

	private final OutputConfiguration config;
	private final Collection<Observer> providers;

	ProviderDataOutputFormatter(OutputConfiguration config, Collection<Observer> providers) {
		this.config = config;
		this.providers = providers;
	}

	public void format(BufferedWriter writer) throws IOException {
		for (Observer provider : this.providers) {
			new ProviderDataRowOutputFormatter(this.config, provider).format(writer);
			writer.write(IOUtils.LINE_SEPARATOR);
		}

	}
}

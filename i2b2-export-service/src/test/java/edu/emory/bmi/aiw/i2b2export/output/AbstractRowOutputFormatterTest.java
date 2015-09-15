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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;

/**
 *
 * @author arpost
 */
public class AbstractRowOutputFormatterTest {
	
	String formatString(RowOutputFormatter formatter) throws IOException, SQLException {
		StringWriter sw = new StringWriter();
		boolean succeeded = false;
		try {
			try (BufferedWriter w = new BufferedWriter(sw)) {
				formatter.format(w);
			}
			sw.close();
			succeeded = true;
		} finally {
			if (!succeeded) {
				try {
					sw.close();
				} catch (IOException ignore) {
				}
			}
		}
		return sw.toString();
	}
}

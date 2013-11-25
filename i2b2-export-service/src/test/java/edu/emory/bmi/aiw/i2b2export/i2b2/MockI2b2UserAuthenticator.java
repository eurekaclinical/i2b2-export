package edu.emory.bmi.aiw.i2b2export.i2b2;

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

import edu.emory.bmi.aiw.i2b2export.comm.I2b2AuthMetadata;
import edu.emory.bmi.aiw.i2b2export.xml.I2b2ExportServiceXmlException;

public class MockI2b2UserAuthenticator implements I2b2UserAuthenticator {

	@Override
	public boolean authenticateUser(I2b2AuthMetadata authMetadata) throws I2b2ExportServiceXmlException {
		return authMetadata.getUsername().equals("test-user");
	}
}

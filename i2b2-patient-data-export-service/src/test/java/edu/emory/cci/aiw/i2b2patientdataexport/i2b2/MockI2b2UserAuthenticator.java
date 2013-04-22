package edu.emory.cci.aiw.i2b2patientdataexport.i2b2;

import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2patientdataexport.xml.I2b2PatientDataExportServiceXmlException;

public class MockI2b2UserAuthenticator implements I2b2UserAuthenticator {

	@Override
	public boolean authenticateUser(I2b2AuthMetadata authMetadata) throws I2b2PatientDataExportServiceXmlException {
		if (authMetadata.getUsername().equals("test-user")) {
			return true;
		} else {
			return false;
		}
	}
}

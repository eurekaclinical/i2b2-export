package edu.emory.cci.aiw.i2b2patientdataexport.i2b2;

import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2patientdataexport.xml.I2b2PatientDataExportServiceXmlException;

public interface I2b2UserAuthenticator {

	public boolean authenticateUser(I2b2AuthMetadata authMetadata) throws
			I2b2PatientDataExportServiceXmlException;
}

package edu.emory.cci.aiw.i2b2patientdataexport.i2b2;

import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2PatientSet;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.I2b2PdoResults;
import edu.emory.cci.aiw.i2b2patientdataexport.xml.I2b2PatientDataExportServiceXmlException;

import java.util.Collection;

public interface I2b2PdoRetriever {

	public I2b2PdoResults retrieve(I2b2AuthMetadata authMetadata,
								   Collection<I2b2Concept> concepts,
								   I2b2PatientSet patientSet) throws
			I2b2PatientDataExportServiceXmlException;
}

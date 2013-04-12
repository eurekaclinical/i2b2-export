package edu.emory.cci.aiw.i2b2patientdataexport.comm;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;

public final class DetailedRequest {
	private I2b2AuthMetadata i2b2AuthMetadata;
	private I2b2PatientSet i2b2PatientSet;
	private OutputConfiguration outputConfiguration;

	public I2b2AuthMetadata getI2b2AuthMetadata() {
		return i2b2AuthMetadata;
	}

	public void setI2b2AuthMetadata(I2b2AuthMetadata i2b2AuthMetadata) {
		this.i2b2AuthMetadata = i2b2AuthMetadata;
	}

	public I2b2PatientSet getI2b2PatientSet() {
		return i2b2PatientSet;
	}

	public void setI2b2PatientSet(I2b2PatientSet i2b2PatientSet) {
		this.i2b2PatientSet = i2b2PatientSet;
	}

	public OutputConfiguration getOutputConfiguration() {
		return outputConfiguration;
	}

	public void setOutputConfiguration(OutputConfiguration outputConfiguration) {
		this.outputConfiguration = outputConfiguration;
	}
}

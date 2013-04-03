package edu.emory.cci.aiw.i2b2datadownloader.comm;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;

public final class DetailedRequest {
	private I2b2AuthMetadata i2b2AuthMetadata;
	private Integer patientSetCollId;
	private OutputConfiguration outputConfiguration;

	public I2b2AuthMetadata getI2b2AuthMetadata() {
		return i2b2AuthMetadata;
	}

	public void setI2b2AuthMetadata(I2b2AuthMetadata i2b2AuthMetadata) {
		this.i2b2AuthMetadata = i2b2AuthMetadata;
	}

	public Integer getPatientSetCollId() {
		return patientSetCollId;
	}

	public void setPatientSetCollId(Integer patientSetCollId) {
		this.patientSetCollId = patientSetCollId;
	}

	public OutputConfiguration getOutputConfiguration() {
		return outputConfiguration;
	}

	public void setOutputConfiguration(OutputConfiguration outputConfiguration) {
		this.outputConfiguration = outputConfiguration;
	}
}

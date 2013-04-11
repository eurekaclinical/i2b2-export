package edu.emory.cci.aiw.i2b2datadownloader.comm;

public final class SummarizedRequest {

	private Long configId;
	private I2b2AuthMetadata i2b2AuthMetadata;
	private I2b2PatientSet i2b2PatientSet;

	public I2b2AuthMetadata getI2b2AuthMetadata() {
		return i2b2AuthMetadata;
	}

	public void setI2b2AuthMetadata(I2b2AuthMetadata i2b2AuthMetadata) {
		this.i2b2AuthMetadata = i2b2AuthMetadata;
	}

	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	public I2b2PatientSet getI2b2PatientSet() {
		return i2b2PatientSet;
	}

	public void setI2b2PatientSet(I2b2PatientSet i2b2PatientSet) {
		this.i2b2PatientSet = i2b2PatientSet;
	}
}

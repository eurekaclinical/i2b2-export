package edu.emory.cci.aiw.i2b2datadownloader.comm;

public final class SummarizedRequest {

	private Long configId;
	private I2b2AuthMetadata i2b2AuthMetadata;
	private Integer patientSetCollId;

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

	public Integer getPatientSetCollId() {
		return patientSetCollId;
	}

	public void setPatientSetCollId(Integer patientSetCollId) {
		this.patientSetCollId = patientSetCollId;
	}
}

package edu.emory.cci.aiw.i2b2datadownloader.comm;

public final class SummarizedRequest {

	private I2b2AuthMetadata i2b2AuthMetadata;
	private String configName;
	private Integer patientSetCollId;

	public I2b2AuthMetadata getI2b2AuthMetadata() {
		return i2b2AuthMetadata;
	}

	public void setI2b2AuthMetadata(I2b2AuthMetadata i2b2AuthMetadata) {
		this.i2b2AuthMetadata = i2b2AuthMetadata;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public Integer getPatientSetCollId() {
		return patientSetCollId;
	}

	public void setPatientSetCollId(Integer patientSetCollId) {
		this.patientSetCollId = patientSetCollId;
	}
}

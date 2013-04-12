package edu.emory.cci.aiw.i2b2datadownloader.comm;

public class LoadRequest {

	private I2b2AuthMetadata authMetadata;
	private Long outputConfigurationId;

	public I2b2AuthMetadata getAuthMetadata() {
		return authMetadata;
	}

	public void setAuthMetadata(I2b2AuthMetadata authMetadata) {
		this.authMetadata = authMetadata;
	}

	public Long getOutputConfigurationId() {
		return outputConfigurationId;
	}

	public void setOutputConfigurationId(Long outputConfigurationId) {
		this.outputConfigurationId = outputConfigurationId;
	}
}

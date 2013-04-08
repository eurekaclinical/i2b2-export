package edu.emory.cci.aiw.i2b2datadownloader.comm;

public class LoadRequest {

	private I2b2AuthMetadata authMetadata;
	private String outputConfigurationName;

	public I2b2AuthMetadata getAuthMetadata() {
		return authMetadata;
	}

	public void setAuthMetadata(I2b2AuthMetadata authMetadata) {
		this.authMetadata = authMetadata;
	}

	public String getOutputConfigurationName() {
		return outputConfigurationName;
	}

	public void setOutputConfigurationName(String outputConfigurationName) {
		this.outputConfigurationName = outputConfigurationName;
	}
}

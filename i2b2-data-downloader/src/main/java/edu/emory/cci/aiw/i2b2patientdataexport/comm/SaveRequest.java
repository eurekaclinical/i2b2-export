package edu.emory.cci.aiw.i2b2datadownloader.comm;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;

public class SaveRequest {

	private OutputConfiguration outputConfiguration;
	private I2b2AuthMetadata i2b2AuthMetadata;

	public OutputConfiguration getOutputConfiguration() {
		return outputConfiguration;
	}

	public void setOutputConfiguration(OutputConfiguration outputConfiguration) {
		this.outputConfiguration = outputConfiguration;
	}

	public I2b2AuthMetadata getI2b2AuthMetadata() {
		return i2b2AuthMetadata;
	}

	public void setI2b2AuthMetadata(I2b2AuthMetadata i2b2AuthMetadata) {
		this.i2b2AuthMetadata = i2b2AuthMetadata;
	}
}

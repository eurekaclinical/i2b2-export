package edu.emory.cci.aiw.i2b2patientdataexport.comm;

public class OutputConfigurationSummary {

	private Long configurationId;
	private String configurationName;

	public OutputConfigurationSummary() {
		this(-1L, "");
	}

	public OutputConfigurationSummary(Long id, String name) {
		this.configurationId = id;
		this.configurationName = name;
	}

	public Long getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(Long configurationId) {
		this.configurationId = configurationId;
	}

	public String getConfigurationName() {
		return configurationName;
	}

	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}
}

package edu.emory.cci.aiw.i2b2datadownloader.comm;

public final class I2b2AuthMetadata {

	private String domain;
	private String username;
	private String passwordNode;
	private String projectId;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordNode() {
		return passwordNode;
	}

	public void setPasswordNode(String passwordNode) {
		this.passwordNode = passwordNode;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}

package edu.emory.cci.aiw.i2b2datadownloader.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

/**
 *
 */
@Entity
@Table(name = "output_configurations")
public class OutputConfiguration {

	@Id
	@SequenceGenerator(name = "OUTPUT_CONFIG_SEQ_GENERATOR",
			sequenceName = "OUTPUT_CONFIG_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "OUTPUT_CONFIG_SEQ_GENERATOR")
	private Long id;

	private Long userId;
	private String name;

	public static enum RowDimension {
		PATIENT, VISIT, PROVIDER;
	}

	private RowDimension rowDimension;
	private String whitespaceReplacement;
	private String separator;
	private String missingValue;

	@OneToMany
	private List<OutputColumnConfiguration> columnConfigs;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RowDimension getRowDimension() {
		return rowDimension;
	}

	public void setRowDimension(RowDimension rowDimension) {
		this.rowDimension = rowDimension;
	}

	public String getWhitespaceReplacement() {
		return whitespaceReplacement;
	}

	public void setWhitespaceReplacement(String whitespaceReplacement) {
		this.whitespaceReplacement = whitespaceReplacement;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getMissingValue() {
		return missingValue;
	}

	public void setMissingValue(String missingValue) {
		this.missingValue = missingValue;
	}

	public List<OutputColumnConfiguration> getColumnConfigs() {
		return columnConfigs;
	}

	public void setColumnConfigs(
			List<OutputColumnConfiguration> columnConfigs) {
		this.columnConfigs = columnConfigs;
	}
}

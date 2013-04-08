package edu.emory.cci.aiw.i2b2datadownloader.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "i2b2_concepts")
public final class I2b2Concept {

	@Id
	@SequenceGenerator(name = "I2B2_CONCEPT_SEQ_GENERATOR",
			sequenceName = "I2B2_CONCEPT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "I2B2_CONCEPT_SEQ_GENERATOR")
	private Long id;

	private String i2b2Key;
	private Integer level;
	private String tableName;
	private String dimensionCode;
	private String isSynonym;

	public I2b2Concept() {
		this(null, null, null, null, null);
	}

	public I2b2Concept(String key, Integer level, String tableName,
					   String dimensionCode, String isSynonym) {
		this.i2b2Key = key;
		this.level = level;
		this.tableName = tableName;
		this.dimensionCode = dimensionCode;
		this.isSynonym = isSynonym;
	}

	public Long getId() {
		return id;
	}

	public String getI2b2Key() {
		return i2b2Key;
	}

	public int getLevel() {
		return level;
	}

	public String getTableName() {
		return tableName;
	}

	public String getDimensionCode() {
		return dimensionCode;
	}

	public String getIsSynonym() {
		return isSynonym;
	}

	public void setI2b2Key(String i2b2Key) {
		this.i2b2Key = i2b2Key;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}

	public void setIsSynonym(String isSynonym) {
		this.isSynonym = isSynonym;
	}

	public int hashCode() {
		return i2b2Key.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof I2b2Concept) {
			I2b2Concept c = (I2b2Concept) o;
			return c.getI2b2Key().equals(this.getI2b2Key());
		}
		return false;
	}

	public String toString() {
		StringBuilder result = new StringBuilder("{");
		result.append("i2b2Key: ");
		result.append(getI2b2Key());
		result.append(", level: ");
		result.append(getLevel());
		result.append(", tableName: ");
		result.append(getTableName());
		result.append(", dimensionCode: ");
		result.append(getDimensionCode());
		result.append(", isSynonym: ");
		result.append(getIsSynonym());
		result.append("}");

		return result.toString();
	}
}

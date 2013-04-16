package edu.emory.cci.aiw.i2b2patientdataexport.entity;

import javax.persistence.*;

@Entity
@Table(name = "i2b2_concepts")
public final class I2b2Concept {

	@Id
	@SequenceGenerator(name = "I2B2_CONCEPT_SEQ_GENERATOR",
			sequenceName = "I2B2_CONCEPT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "I2B2_CONCEPT_SEQ_GENERATOR")
	private Long id;

	@Column(nullable = false)
	private String i2b2Key;

	@Column(nullable = false)
	private Integer level;

	@Column(nullable = false)
	private String dimensionCode;

	@Column(nullable = false)
	private String tableName;

	@Column(nullable = false)
	private String isSynonym;

	private String name;
	private String columnName;
	private String operator;
	private String displayName;
	private String tooltip;
	private String hasChildren;
	private String icd9;

    @Column(columnDefinition = "CLOB")
    @Lob
    private String xmlOrig;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(String hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getSynonym() {
        return isSynonym;
    }

    public void setSynonym(String synonym) {
        isSynonym = synonym;
    }

    public String getIcd9() {
        return icd9;
    }

    public void setIcd9(String icd9) {
        this.icd9 = icd9;
    }

    public String getXmlOrig() {
        return xmlOrig;
    }

    public void setXmlOrig(String xmlOrig) {
        this.xmlOrig = xmlOrig;
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
package edu.emory.cci.aiw.i2b2datadownloader.entity;

public final class I2b2Concept {
    private String key;
    private Integer level;
    private String tableName;
    private String dimensionCode;
    private String isSynonym;

	public I2b2Concept() {
		this(null, null, null, null, null);
	}

    public I2b2Concept(String key, Integer level, String tableName,
					   String dimensionCode, String isSynonym) {
        this.key = key;
        this.level = level;
        this.tableName = tableName;
        this.dimensionCode = dimensionCode;
        this.isSynonym = isSynonym;
    }

    public String getKey() {
        return key;
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

	public void setKey(String key) {
		this.key = key;
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

	public void setSynonym(String synonym) {
		isSynonym = synonym;
	}

	public int hashCode() {
        return key.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof I2b2Concept) {
            I2b2Concept c = (I2b2Concept) o;
            return c.getKey().equals(this.getKey());
        }
        return false;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("{");
        result.append("key: ");
        result.append(getKey());
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

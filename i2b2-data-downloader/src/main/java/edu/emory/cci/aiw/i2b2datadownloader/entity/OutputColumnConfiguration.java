package edu.emory.cci.aiw.i2b2datadownloader.entity;

/**
 *
 */
public class OutputColumnConfiguration {
	private Integer order;
	private String i2b2ConceptPath;
	private String columnName;

	public static enum DisplayFormat {
		EXISTENCE, VALUE, AGGREGATION;
	}

	private DisplayFormat displayFormat;
	private Integer howMany;
	private Boolean includeUnits;
	private Boolean includeTimeRange;

	public static enum AggregationType {
		MIN, MAX, AVG;
	}

	private AggregationType aggregation;

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getI2b2ConceptPath() {
		return i2b2ConceptPath;
	}

	public void setI2b2ConceptPath(String i2b2ConceptPath) {
		this.i2b2ConceptPath = i2b2ConceptPath;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public DisplayFormat getDisplayFormat() {
		return displayFormat;
	}

	public void setDisplayFormat(
			DisplayFormat displayFormat) {
		this.displayFormat = displayFormat;
	}

	public Integer getHowMany() {
		return howMany;
	}

	public void setHowMany(Integer howMany) {
		this.howMany = howMany;
	}

	public Boolean getIncludeUnits() {
		return includeUnits;
	}

	public void setIncludeUnits(Boolean includeUnits) {
		this.includeUnits = includeUnits;
	}

	public Boolean getIncludeTimeRange() {
		return includeTimeRange;
	}

	public void setIncludeTimeRange(Boolean includeTimeRange) {
		this.includeTimeRange = includeTimeRange;
	}

	public AggregationType getAggregation() {
		return aggregation;
	}

	public void setAggregation(
			AggregationType aggregation) {
		this.aggregation = aggregation;
	}
}

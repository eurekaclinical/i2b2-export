package edu.emory.cci.aiw.i2b2datadownloader.entity;

/**
 *
 */
public class OutputColumnConfiguration implements
		Comparable<OutputColumnConfiguration> {
	private Integer order;
	private I2b2Concept i2b2Concept;
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

	public I2b2Concept getI2b2Concept() {
		return i2b2Concept;
	}

	public void setI2b2Concept(I2b2Concept i2b2Concept) {
		this.i2b2Concept = i2b2Concept;
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

	@Override
	public int compareTo(OutputColumnConfiguration other) {
		return this.order.compareTo(other.getOrder());
	}
}
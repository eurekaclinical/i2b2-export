package edu.emory.cci.aiw.i2b2patientdataexport.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "output_column_configurations")
public class OutputColumnConfiguration implements
		Comparable<OutputColumnConfiguration> {

	@Id
	@SequenceGenerator(name = "OUTPUT_COL_CONFIG_SEQ_GENERATOR",
			sequenceName = "OUTPUT_COL_CONFIG_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "OUTPUT_COL_CONFIG_SEQ_GENERATOR")
	private Long id;

	@Column(nullable = false)
	private Integer columnOrder;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private I2b2Concept i2b2Concept;

	@Column(nullable = false)
	private String columnName;

	public static enum DisplayFormat {
		EXISTENCE, VALUE, AGGREGATION;
	}

	@Column(nullable = false)
	private DisplayFormat displayFormat;

	private Integer howMany;
	private Boolean includeUnits;
	private Boolean includeTimeRange;

	public static enum AggregationType {
		MIN, MAX, AVG;
	}

	private AggregationType aggregation;

	public Long getId() {
		return id;
	}

	public Integer getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(Integer columnOrder) {
		this.columnOrder = columnOrder;
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
		return this.columnOrder.compareTo(other.getColumnOrder());
	}
}
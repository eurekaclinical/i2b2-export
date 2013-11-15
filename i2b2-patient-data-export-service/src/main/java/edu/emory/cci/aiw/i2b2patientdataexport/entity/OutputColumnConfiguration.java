package edu.emory.cci.aiw.i2b2patientdataexport.entity;

/*
 * #%L
 * i2b2 Patient Data Export Service
 * %%
 * Copyright (C) 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.codehaus.jackson.annotate.JsonBackReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "column_configs")
public class OutputColumnConfiguration implements
		Comparable<OutputColumnConfiguration> {

	@Id
	@SequenceGenerator(name = "OUTPUT_COL_CONFIG_SEQ_GENERATOR",
			sequenceName = "OUTPUT_COL_CONFIG_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO,
			generator = "OUTPUT_COL_CONFIG_SEQ_GENERATOR")
	@Column(name = "column_config_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "config_id")
	private OutputConfiguration outputConfig;

	@Column(name = "column_order", nullable = false)
	private Integer columnOrder;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "i2b2_concept_id")
	private I2b2Concept i2b2Concept;

	@Column(name = "column_name", nullable = false)
	private String columnName;

	public static enum DisplayFormat {
		EXISTENCE, VALUE, AGGREGATION;
	}

	@Column(name = "display_format", nullable = false)
	private DisplayFormat displayFormat;

	@Column(name = "how_many")
	private Integer howMany;

	@Column(name = "include_units")
	private Boolean includeUnits;

	@Column(name = "include_time_range")
	private Boolean includeTimeRange;

	public static enum AggregationType {
		MIN, MAX, AVG;
	}

	@Column(name = "aggregation")
	private AggregationType aggregation;

	public Long getId() {
		return id;
	}

	@JsonBackReference
	public OutputConfiguration getOutputConfig() {
		return outputConfig;
	}

	public void setOutputConfig(OutputConfiguration outputConfig) {
		this.outputConfig = outputConfig;
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
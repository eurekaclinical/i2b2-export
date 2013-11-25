package edu.emory.bmi.aiw.i2b2export.entity;

/*
 * #%L
 * i2b2 Export Service
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

import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

/**
 * A JPA entity for representing output configurations.
 *
 * @author Michel Mansour
 * @since 1.0
 */
@Entity
@Table(name = "output_configs", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"username", "config_name"})})
public class OutputConfiguration {

	@Id
	@SequenceGenerator(name = "OUTPUT_CONFIG_SEQ_GENERATOR",
			sequenceName = "OUTPUT_CONFIG_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO,
			generator = "OUTPUT_CONFIG_SEQ_GENERATOR")
	@Column(name = "config_id")
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(name = "config_name")
	private String name;

	/**
	 * Valid row dimensin values
	 */
	public static enum RowDimension {
		/**
		 * each row represents a patient
		 */
		PATIENT,

		/**
		 * each row represents a visit
		 */
		VISIT,

		/**
		 * each row represents a provider
		 */
		PROVIDER
	}

	@Column(name = "row_dimension", nullable = false)
	private RowDimension rowDimension;

	@Column(name = "separator", length = 1)
	private String separator;

	@Column(name = "quote_char", length = 1)
	private String quoteChar;

	@Column(name = "whitespace_replacement")
	private String whitespaceReplacement;

	@Column(name = "missing_value")
	private String missingValue;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "outputConfig")
	private List<OutputColumnConfiguration> columnConfigs;

	/**
	 * Gets the ID of the output configuration record.
	 *
	 * @return the ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the ID of the output configuration record.
	 *
	 * @param id the ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the username of the user associated with this configuration.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the name of the configuration
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the configuration
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the row dimension.
	 *
	 * @return the row dimension
	 */
	public RowDimension getRowDimension() {
		return rowDimension;
	}

	/**
	 * Sets the row dimension.
	 *
	 * @param rowDimension the row dimension
	 */
	public void setRowDimension(RowDimension rowDimension) {
		this.rowDimension = rowDimension;
	}

	/**
	 * Gets the header row whitespace replacement string.
	 *
	 * @return the whitespace replacement string
	 */
	public String getWhitespaceReplacement() {
		return whitespaceReplacement;
	}

	/**
	 * Sets the whitespace replacement string.
	 *
	 * @param whitespaceReplacement the whitespace replacement string
	 */
	public void setWhitespaceReplacement(String whitespaceReplacement) {
		this.whitespaceReplacement = whitespaceReplacement;
	}

	/**
	 * Gets the column separator/delimiter.
	 *
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Sets the separator.
	 *
	 * @param separator the separator
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * Gets the character used to quote the data.
	 *
	 * @return the quote character
	 */
	public String getQuoteChar() {
		return quoteChar;
	}

	/**
	 * Sets the quote character
	 *
	 * @param quoteChar the quote character
	 */
	public void setQuoteChar(String quoteChar) {
		this.quoteChar = quoteChar;
	}

	/**
	 * Gets the string to use when data is missing for a column.
	 *
	 * @return the missing value string
	 */
	public String getMissingValue() {
		return missingValue;
	}

	/**
	 * Sets the missing value string.
	 *
	 * @param missingValue the missing value string
	 */
	public void setMissingValue(String missingValue) {
		this.missingValue = missingValue;
	}

	/**
	 * Gets the list of output column configurations that make up this output configuration.
	 *
	 * @return a list of {@link OutputColumnConfiguration}s
	 */
	@JsonManagedReference
	public List<OutputColumnConfiguration> getColumnConfigs() {
		return columnConfigs;
	}

	/**
	 * Sets the list of output column configurations.
	 *
	 * @param columnConfigs the output column configurations
	 */
	public void setColumnConfigs(List<OutputColumnConfiguration> columnConfigs) {
		this.columnConfigs = columnConfigs;
	}
}

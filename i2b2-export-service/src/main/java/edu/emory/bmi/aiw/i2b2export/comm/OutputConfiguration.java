package edu.emory.bmi.aiw.i2b2export.comm;

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
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.RowDimension;
import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonManagedReference;

import java.util.List;

/**
 * A JPA entity for representing output configurations.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class OutputConfiguration {

	private Long id;

	private String username;

	private String name;

	private RowDimension rowDimension;

	private String separator = "\t";

	private String quoteChar;

	private String whitespaceReplacement;

	private String missingValue;

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
		if (separator == null) {
			this.separator = "\t";
		} else {
			this.separator = separator;
		}
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
	 * Gets the list of output column configurations that make up this output
	 * configuration.
	 *
	 * @return a list of {@link OutputColumnConfiguration}s
	 */
	public List<OutputColumnConfiguration> getColumnConfigs() {
		return columnConfigs;
	}

	/**
	 * Sets the list of output column configurations.
	 *
	 * @param columnConfigs the output column configurations
	 */
	@JsonManagedReference
	public void setColumnConfigs(List<OutputColumnConfiguration> columnConfigs) {
		this.columnConfigs = columnConfigs;
	}

	public OutputConfigurationEntity toEntity() {
		OutputConfigurationEntity result = new OutputConfigurationEntity();
		List<OutputColumnConfigurationEntity> columnConfigs = new ArrayList<>();
		if (this.columnConfigs != null) {
			for (OutputColumnConfiguration columnConfig : this.columnConfigs) {
				columnConfigs.add(columnConfig.toEntity());
			}
		}
		result.setColumnConfigs(columnConfigs);
		result.setId(this.id);
		result.setMissingValue(this.missingValue);
		result.setName(this.name);
		result.setQuoteChar(this.quoteChar);
		result.setRowDimension(this.rowDimension);
		result.setSeparator(this.separator);
		result.setUsername(this.username);
		result.setWhitespaceReplacement(this.whitespaceReplacement);
		return result;
	}
}

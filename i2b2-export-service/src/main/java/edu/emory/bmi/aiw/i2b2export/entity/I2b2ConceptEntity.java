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

import edu.emory.bmi.aiw.i2b2export.comm.I2b2Concept;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A JPA entity representing an i2b2 concept.
 *
 * @author Michel Mansour
 * @since 1.0
 */
@Entity
@Table(name = "i2b2_concepts")
public class I2b2ConceptEntity {

	@Id
	@SequenceGenerator(name = "I2B2_CONCEPT_SEQ_GENERATOR",
			sequenceName = "I2B2_CONCEPT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO,
			generator = "I2B2_CONCEPT_SEQ_GENERATOR")
	@Column(name = "concept_id")
	private Long id;

	@Column(name = "i2b2_key", nullable = false)
	private String i2b2Key;

	@Column(name = "c_hlevel", nullable = false)
	private Integer level;

	@Column(name = "c_dimcode", nullable = false)
	private String dimensionCode;

	@Column(name = "c_tablename", nullable = false)
	private String tableName;

	@Column(name = "c_synonym_cd", nullable = false)
	private String isSynonym;

	@Column(name = "c_name")
	private String name;

	@Column(name = "c_columnname")
	private String columnName;

	@Column(name = "c_operator")
	private String operator;

	@Column(name = "c_displayname")
	private String displayName;

	@Column(name = "c_tooltip")
	private String tooltip;

	@Column(name = "c_children")
	private String hasChildren;

	@Column(name = "c_icd9")
	private String icd9;

	@Column(name = "c_xml_orig", columnDefinition = "CLOB")
	@Lob
	private String xmlOrig;

	/**
	 * Default no-arg constructor. Sets all fields to null. Modifiers should be called immediately after.
	 */
	public I2b2ConceptEntity() {
		this(null, null, null, null, null);
	}

	/**
	 * Constructs an i2b2 concept from required fields.
	 *
	 * @param key the concept key
	 * @param level the concept's level in the hierarchy
	 * @param tableName the concept's table name
	 * @param dimensionCode the concept's dimension code
	 * @param isSynonym the concept's synonym code
	 */
	public I2b2ConceptEntity(String key, Integer level, String tableName,
					String dimensionCode, String isSynonym) {
		this.i2b2Key = key;
		this.level = level;
		this.tableName = tableName;
		this.dimensionCode = dimensionCode;
		this.isSynonym = isSynonym;
	}

	/**
	 * Gets the JPA entity's ID
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the i2b2 key.
	 *
	 * @return the i2b2 key
	 */
	public String getI2b2Key() {
		return i2b2Key;
	}

	/**
	 * Gets the i2b2 concept's level in the hierarchy.
	 *
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the i2b2 concept's table name.
	 *
	 * @return the table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Gets the i2b2 concept's dimension code.
	 *
	 * @return the dimension code
	 */
	public String getDimensionCode() {
		return dimensionCode;
	}

	/**
	 * Gets the i2b2 concept's synonym code.
	 *
	 * @return the synonym code
	 */
	public String getIsSynonym() {
		return isSynonym;
	}

	/**
	 * Sets the i2b2 key.
	 *
	 * @param i2b2Key the i2b2 key
	 */
	public void setI2b2Key(String i2b2Key) {
		this.i2b2Key = i2b2Key;
	}

	/**
	 * Sets the level.
	 *
	 * @param level the level
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * Sets the table name.
	 *
	 * @param tableName the table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Sets the dimension code.
	 *
	 * @param dimensionCode the dimension code
	 */
	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}

	/**
	 * Sets the synonym code.
	 *
	 * @param isSynonym the synonym code
	 */
	public void setIsSynonym(String isSynonym) {
		this.isSynonym = isSynonym;
	}

	/**
	 * Gets the concept name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the concept column name.
	 *
	 * @return the column name
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * Sets the column name.
	 *
	 * @param columnName the column name
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator the operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the display name for the concept.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName the display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the concept's tooltip.
	 *
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Sets the tooltip.
	 *
	 * @param tooltip the tooltip
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * Gets whether this concept has children.
	 *
	 * @return whether it has children
	 */
	public String getHasChildren() {
		return hasChildren;
	}

	/**
	 * Sets the has children property.
	 *
	 * @param hasChildren whether it has children
	 */
	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}

	/**
	 * Gets the concept's ICD-9 form.
	 *
	 * @return the ICD-9 string
	 */
	public String getIcd9() {
		return icd9;
	}

	/**
	 * Sets the concept's ICD-9 string.
	 *
	 * @param icd9 the ICD-9 string
	 */
	public void setIcd9(String icd9) {
		this.icd9 = icd9;
	}

	/**
	 * Gets the original XML of the concept.
	 *
	 * @return the original XML
	 */
	public String getXmlOrig() {
		return xmlOrig;
	}

	/**
	 * Sets the original XML.
	 *
	 * @param xmlOrig the original XML
	 */
	public void setXmlOrig(String xmlOrig) {
		this.xmlOrig = xmlOrig;
	}
	
	public String getColumnDataType() {
		return "T";
	}
	
	public String getFactTableColumn() {
		String tableNameUC = StringUtils.upperCase(this.tableName);
		if ("CONCEPT_DIMENSION".equals(tableNameUC)) {
			return "concept_cd";
		} else if ("PATIENT_DIMENSION".equals(tableNameUC)) {
			return "patient_num";
		} else {
			throw new IllegalStateException("Unexpected table name " + this.tableName);
		}
	}

	@Override
	public int hashCode() {
		return i2b2Key.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof I2b2ConceptEntity) {
			I2b2ConceptEntity c = (I2b2ConceptEntity) o;
			return c.getI2b2Key().equals(this.getI2b2Key());
		}
		return false;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	I2b2Concept toDTO() {
		I2b2Concept result = new I2b2Concept();
		result.setColumnName(this.columnName);
		result.setDimensionCode(this.dimensionCode);
		result.setDisplayName(this.displayName);
		result.setHasChildren(this.hasChildren);
		result.setI2b2Key(this.i2b2Key);
		result.setIcd9(this.icd9);
		result.setIsSynonym(this.isSynonym);
		result.setLevel(this.level);
		result.setName(this.name);
		result.setOperator(this.operator);
		result.setTableName(this.tableName);
		result.setTooltip(this.tooltip);
		result.setXmlOrig(this.xmlOrig);
		return result;
	}
}

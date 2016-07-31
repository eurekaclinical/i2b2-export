package edu.emory.bmi.aiw.i2b2export.output;

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
import edu.emory.bmi.aiw.i2b2export.entity.I2b2ConceptEntity;
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfigurationEntity;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.arp.javautil.sql.DatabaseProduct;
import org.eurekaclinical.i2b2.client.pdo.Observation;
import org.eurekaclinical.i2b2.client.pdo.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for formatting a row of output. Depending on what a row
 * represents (patient, visit, or provider), the first columns of the row will
 * differ, as well as which observations should be displayed. Methods for
 * computing those values are defined as abstract in this class. However, the
 * actual formatting of the row is the same across all implementations.
 *
 * @author Michel Mansour
 * @since 1.0
 */
abstract class DataRowOutputFormatter extends AbstractFormatter implements RowOutputFormatter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataRowOutputFormatter.class);

	private final OutputConfigurationEntity config;
	private final FormatOptions formatOptions;
	private final Connection con;
	private DatabaseProduct databaseProduct;

	DataRowOutputFormatter(Connection con, OutputConfigurationEntity config) {
		super(config);
		this.config = config;
		this.formatOptions = new FormatOptions(config);
		this.con = con;
	}

	final OutputConfigurationEntity getConfig() {
		return config;
	}

	final FormatOptions getFormatOptions() {
		return formatOptions;
	}

	/**
	 * Finds the observations that match the given i2b2 concept path.
	 *
	 * @param i2b2Concept the i2b2 concept to match
	 * @return an unmodifiable {@link Collection} of {@link Observation}s that
	 * match the given concept path
	 */
	abstract Collection<Observation> matchingObservations(I2b2ConceptEntity i2b2Concept) throws SQLException;

	/**
	 * Generates the first fields of the row that depend on the row dimension
	 * rather than the data, for example, patient id, visit id, etc. The return
	 * value is expected to be already joined by the column separator specified
	 * by the output configuration.
	 *
	 * @return the first fields of the row, as a {@link String}, joined by the
	 * column separator specified in the output configuration
	 */
	abstract int rowPrefix(BufferedWriter writer) throws IOException;

	/**
	 * Formats a row of data according to the instance's column configurations
	 * and format options. The result is returned as an array of strings that
	 * can be joined together later with the correct delimiter.
	 *
	 * @return an array of {@link String}s representing a single row of output
	 */
	@Override
	public final void format(BufferedWriter writer) throws IOException, SQLException {
		int colNum = rowPrefix(writer);
		for (OutputColumnConfigurationEntity colConfig : getConfig().getColumnConfigs()) {
			Collection<Observation> obxs = matchingObservations(colConfig
					.getI2b2Concept());
			switch (colConfig.getDisplayFormat()) {
				case EXISTENCE:
					colNum = new ExistenceColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs, writer, colNum);
					break;
				case VALUE:
					colNum = new ValueColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs, writer, colNum);
					break;
				case AGGREGATION:
					colNum = new AggregationColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs, writer, colNum);
					break;
				default:
					throw new RuntimeException("display format not provided");
			}
		}

	}

	boolean compareDimensionColumnValue(I2b2ConceptEntity i2b2Concept, Patient patient) throws SQLException {
		String op = i2b2Concept.getOperator();
		String columnDataType = i2b2Concept.getColumnDataType();
		String dimCode = i2b2Concept.getDimensionCode();
		boolean dimCodeNeedsQuotes = needsQuotes(columnDataType, dimCode);
		String stmtFrag = op
				+ " "
				+ ("IN".equalsIgnoreCase(op)
					? "("
					: "")
				+ ("BETWEEN".equalsIgnoreCase(op)
					? dimCode
					: quoteIfNeeded(dimCodeNeedsQuotes, dimCode))
				+ ("IN".equalsIgnoreCase(op)
					? ")"
					: "");
		String birthDate = patient.getBirthDate();
		String stmt;
		if (this.databaseProduct == null) {
			this.databaseProduct = DatabaseProduct.fromMetaData(con.getMetaData());
		}
		if (this.databaseProduct == DatabaseProduct.POSTGRESQL) {
			stmt = "SELECT "
					+ ("birth_date".equalsIgnoreCase(i2b2Concept.getColumnName()) && birthDate != null
						? birthDate + "::timestamptz"
						: quoteIfNeeded(columnDataType, getParam(patient, i2b2Concept)))
					+ " "
					+ stmtFrag;
		} else {
			stmt = "SELECT "
					+ ("birth_date".equalsIgnoreCase(i2b2Concept.getColumnName()) && birthDate != null
						? "parsedatetime('" + birthDate + "', 'yyyy-MM-dd''T''HH:mm:ss.SSSX')"
						: quoteIfNeeded(columnDataType, getParam(patient, i2b2Concept)))
					+ " "
					+ stmtFrag
					+ " FROM DUAL";
		}
		LOGGER.debug("SQL statement {}", stmt);
		try (Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery(stmt)) {
			if (rs.next()) {
				return rs.getBoolean(1);
			} else {
				return false;
			}
		}
	}

	String getParam(Patient patient, I2b2ConceptEntity i2b2Concept) {
		switch (StringUtils.lowerCase(i2b2Concept.getColumnName())) {
			case "age_in_years_num":
				return patient.getAgeInYears();
			case "birth_date":
				return patient.getBirthDate();
			case "language_cd":
				return patient.getLanguage();
			case "marital_status_cd":
				return patient.getMaritalStatus();
			case "religion_cd":
				return patient.getReligion();
			case "race_cd":
				return patient.getRace();
			case "sex_cd":
				return patient.getSex();
			case "statecityzip_path_char":
				return patient.getStateCityZip();
			case "vital_status_cd":
				return patient.getVitalStatus();
			case "zipcode_char":
				return patient.getZipCode();
			default:
				return "";
		}
	}

	private boolean needsQuotes(String columnDataType, String str) {
		return "T".equals(columnDataType) && str != null && !str.startsWith("'");
	}

	private String quoteIfNeeded(boolean needsQuotes, String str) {
		return (needsQuotes ? "'" : "") + str + (needsQuotes ? "'" : "");
	}

	private String quoteIfNeeded(String columnDataType, String str) {
		boolean needsQuotes = needsQuotes(columnDataType, str);
		return (needsQuotes ? "'" : "") + str + (needsQuotes ? "'" : "");
	}
}

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

import edu.emory.bmi.aiw.i2b2export.entity.DisplayFormat;
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.RowDimension;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.eurekaclinical.i2b2.client.I2b2CommUtil;
import org.eurekaclinical.i2b2.client.pdo.Event;
import org.eurekaclinical.i2b2.client.pdo.Observation;
import org.eurekaclinical.i2b2.client.pdo.Patient;

public class ValueColumnOutputFormatterTest extends AbstractColumnFormatterTest {

	private OutputColumnConfigurationEntity colConfig;
	private FormatOptions formatOptions;

	public ValueColumnOutputFormatterTest() throws ParseException {
		final DateFormat i2b2DateFormat = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);

		colConfig = new OutputColumnConfigurationEntity();
		colConfig.setColumnOrder(0);
		colConfig.setDisplayFormat(DisplayFormat.VALUE);

		OutputConfigurationEntity config = new OutputConfigurationEntity();
		config.setName("foo");
		config.setUsername("i2b2");
		config.setRowDimension(RowDimension.PATIENT);
		config.setSeparator(",");
		config.setQuoteChar("\"");
		config.setMissingValue("(NULL)");
		config.setWhitespaceReplacement("_");
		config.setColumnConfigs(new ArrayList<>());

		colConfig.setOutputConfig(config);
		formatOptions = new FormatOptions(config);

		Patient p = new Patient.Builder("1").build();
		Event e = new Event.Builder("1", p).build();

		Collection<Observation> obxs = new ArrayList<>();
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-01-01T09:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-01-01T10:00:00.000-0500")).tval("100").units("U").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-02-02T10:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-02-02T11:00:00.000-0500")).tval("200").units("V").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-03-03T11:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-03-03T12:00:00.000-0500")).tval("300").units("W").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-04-04T12:00:00.000-0400")).endDate(i2b2DateFormat.parse("2013-04-04T13:00:00.000-0400")).tval("400").units("X").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-05-05T13:00:00.000-0400")).endDate(i2b2DateFormat.parse("2013-05-05T14:00:00.000-0400")).tval("500").units("Y").build());
		setObxs(obxs);
	}

	@Test
	public void testFormatSimple() throws IOException {
		colConfig.setHowMany(1);
		colConfig.setIncludeUnits(false);
		colConfig.setIncludeTimeRange(false);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		Assert.assertEquals("\"500\"", formatString(formatter));
	}

	@Test
	public void testFormatUnits() throws IOException {
		colConfig.setHowMany(1);
		colConfig.setIncludeUnits(true);
		colConfig.setIncludeTimeRange(false);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);
		Assert.assertEquals("\"500\",\"Y\"", formatString(formatter));
	}

	@Test
	public void testFormatDateRange() throws IOException {
		colConfig.setHowMany(1);
		colConfig.setIncludeUnits(false);
		colConfig.setIncludeTimeRange(true);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);
		Assert.assertEquals("\"500\",\"2013-05-05T13:00:00.000-0400\",\"2013-05-05T14:00:00.000-0400\"", formatString(formatter));
	}

	@Test
	public void testFormatUnitsDateRange() throws IOException {
		colConfig.setHowMany(1);
		colConfig.setIncludeUnits(true);
		colConfig.setIncludeTimeRange(true);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);
		Assert.assertEquals("\"500\",\"Y\",\"2013-05-05T13:00:00.000-0400\",\"2013-05-05T14:00:00.000-0400\"", formatString(formatter));
	}

	@Test
	public void testFormatMultEqual() throws IOException {
		colConfig.setHowMany(5);
		colConfig.setIncludeUnits(false);
		colConfig.setIncludeTimeRange(false);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);
		Assert.assertEquals("\"500\",\"400\",\"300\",\"200\",\"100\"", formatString(formatter));
	}

	@Test
	public void testFormatMultLess() throws IOException {
		colConfig.setHowMany(3);
		colConfig.setIncludeUnits(false);
		colConfig.setIncludeTimeRange(false);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		List<String> expected = new ArrayList<>();
		expected.add("500");
		expected.add("400");
		expected.add("300");
		Assert.assertEquals("\"500\",\"400\",\"300\"", formatString(formatter));
	}

	@Test
	public void testFormatMultMore() throws IOException {
		colConfig.setHowMany(7);
		colConfig.setIncludeUnits(false);
		colConfig.setIncludeTimeRange(false);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);
		Assert.assertEquals("\"500\",\"400\",\"300\",\"200\",\"100\",\"(NULL)\",\"(NULL)\"", formatString(formatter));
	}

	@Test
	public void testFormatMultUnitsDateRange() throws IOException {
		colConfig.setHowMany(7);
		colConfig.setIncludeUnits(true);
		colConfig.setIncludeTimeRange(true);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		List<String> expected = new ArrayList<>();
		expected.add("500");
		expected.add("Y");
		expected.add("2013-05-05T13:00:00.000-0400");
		expected.add("2013-05-05T14:00:00.000-0400");
		expected.add("400");
		expected.add("X");
		expected.add("2013-04-04T12:00:00.000-0400");
		expected.add("2013-04-04T13:00:00.000-0400");
		expected.add("300");
		expected.add("W");
		expected.add("2013-03-03T11:00:00.000-0500");
		expected.add("2013-03-03T12:00:00.000-0500");
		expected.add("200");
		expected.add("V");
		expected.add("2013-02-02T10:00:00.000-0500");
		expected.add("2013-02-02T11:00:00.000-0500");
		expected.add("100");
		expected.add("U");
		expected.add("2013-01-01T09:00:00.000-0500");
		expected.add("2013-01-01T10:00:00.000-0500");
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		Assert.assertEquals("\"" + StringUtils.join(expected, "\",\"") + "\"", formatString(formatter));
	}

	@Test
	public void testFormatEmpty() throws IOException {
		colConfig.setHowMany(2);
		colConfig.setIncludeTimeRange(true);
		colConfig.setIncludeUnits(true);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);
		
		Assert.assertEquals("\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"", formatStringTestFormatEmpty(formatter));
	}
	
	String formatStringTestFormatEmpty(ValueColumnOutputFormatter formatter) throws IOException {
		StringWriter sw = new StringWriter();
		boolean succeeded = false;
		try {
			try (BufferedWriter w = new BufferedWriter(sw)) {
				formatter.format(new ArrayList<Observation>(), w, 0);
			}
			sw.close();
			succeeded = true;
		} finally {
			if (!succeeded) {
				try {
					sw.close();
				} catch (IOException ignore) {
				}
			}
		}
		return sw.toString();
	}
}

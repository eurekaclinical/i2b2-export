package edu.emory.cci.aiw.i2b2export.output;

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

import edu.emory.cci.aiw.i2b2export.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2export.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2export.i2b2.I2b2CommUtil;
import edu.emory.cci.aiw.i2b2export.i2b2.pdo.Event;
import edu.emory.cci.aiw.i2b2export.i2b2.pdo.Observation;
import edu.emory.cci.aiw.i2b2export.i2b2.pdo.Patient;
import junit.framework.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ValueColumnOutputFormatterTest {

	private OutputConfiguration config;
	private OutputColumnConfiguration colConfig;
	private FormatOptions formatOptions;
	private Collection<Observation> obxs = new ArrayList<Observation>();

	public ValueColumnOutputFormatterTest() throws ParseException {
		final DateFormat i2b2DateFormat = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);

		colConfig = new OutputColumnConfiguration();
		colConfig.setColumnOrder(0);
		colConfig.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);

		config = new OutputConfiguration();
		config.setName("foo");
		config.setUsername("i2b2");
		config.setRowDimension(OutputConfiguration.RowDimension.PATIENT);
		config.setSeparator(",");
		config.setQuoteChar("\"");
		config.setMissingValue("(NULL)");
		config.setWhitespaceReplacement("_");
		config.setColumnConfigs(new ArrayList<OutputColumnConfiguration>());

		colConfig.setOutputConfig(config);
		formatOptions = new FormatOptions(config);

		Patient p = new Patient.Builder("1").build();
		Event e = new Event.Builder("1", p).build();

		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-01-01T09:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-01-01T10:00:00.000-0500")).tval("100").units("U").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-02-02T10:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-02-02T11:00:00.000-0500")).tval("200").units("V").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-03-03T11:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-03-03T12:00:00.000-0500")).tval("300").units("W").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-04-04T12:00:00.000-0400")).endDate(i2b2DateFormat.parse("2013-04-04T13:00:00.000-0400")).tval("400").units("X").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-05-05T13:00:00.000-0400")).endDate(i2b2DateFormat.parse("2013-05-05T14:00:00.000-0400")).tval("500").units("Y").build());
	}

	@Test
	public void testFormatSimple() {
		colConfig.setHowMany(1);
		colConfig.setIncludeUnits(false);
		colConfig.setIncludeTimeRange(false);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		Assert.assertEquals(Collections.singletonList("500"), formatter.format(obxs));
	}

	@Test
	public void testFormatUnits() {
		colConfig.setHowMany(1);
		colConfig.setIncludeUnits(true);
		colConfig.setIncludeTimeRange(false);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		List<String> expected = new ArrayList<String>();
		expected.add("500");
		expected.add("Y");
		Assert.assertEquals(expected, formatter.format(obxs));
	}

	@Test
	public void testFormatDateRange() {
		colConfig.setHowMany(1);
		colConfig.setIncludeUnits(false);
		colConfig.setIncludeTimeRange(true);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		List<String> expected = new ArrayList<String>();
		expected.add("500");
		expected.add("2013-05-05T13:00:00.000-0400");
		expected.add("2013-05-05T14:00:00.000-0400");
		Assert.assertEquals(expected, formatter.format(obxs));
	}

	@Test
	public void testFormatUnitsDateRange() {
		colConfig.setHowMany(1);
		colConfig.setIncludeUnits(true);
		colConfig.setIncludeTimeRange(true);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		List<String> expected = new ArrayList<String>();
		expected.add("500");
		expected.add("Y");
		expected.add("2013-05-05T13:00:00.000-0400");
		expected.add("2013-05-05T14:00:00.000-0400");
		Assert.assertEquals(expected, formatter.format(obxs));
	}

	@Test
	public void testFormatMultEqual() {
		colConfig.setHowMany(5);
		colConfig.setIncludeUnits(false);
		colConfig.setIncludeTimeRange(false);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		List<String> expected = new ArrayList<String>();
		expected.add("500");
		expected.add("400");
		expected.add("300");
		expected.add("200");
		expected.add("100");
		Assert.assertEquals(expected, formatter.format(obxs));
	}

	@Test
	public void testFormatMultLess() {
		colConfig.setHowMany(3);
		colConfig.setIncludeUnits(false);
		colConfig.setIncludeTimeRange(false);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		List<String> expected = new ArrayList<String>();
		expected.add("500");
		expected.add("400");
		expected.add("300");
		Assert.assertEquals(expected, formatter.format(obxs));
	}

	@Test
	public void testFormatMultMore() {
		colConfig.setHowMany(7);
		colConfig.setIncludeUnits(false);
		colConfig.setIncludeTimeRange(false);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		List<String> expected = new ArrayList<String>();
		expected.add("500");
		expected.add("400");
		expected.add("300");
		expected.add("200");
		expected.add("100");
		expected.add("(NULL)");
		expected.add("(NULL)");
		Assert.assertEquals(expected, formatter.format(obxs));
	}

	@Test
	public void testFormatMultUnitsDateRange() {
		colConfig.setHowMany(7);
		colConfig.setIncludeUnits(true);
		colConfig.setIncludeTimeRange(true);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		List<String> expected = new ArrayList<String>();
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
		Assert.assertEquals(expected, formatter.format(obxs));
	}

	@Test
	public void testFormatEmpty() {
		colConfig.setHowMany(2);
		colConfig.setIncludeTimeRange(true);
		colConfig.setIncludeUnits(true);

		ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

		List<String> expected = new ArrayList<String>();
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		expected.add("(NULL)");
		Assert.assertEquals(expected, formatter.format(new ArrayList<Observation>()));
	}
}

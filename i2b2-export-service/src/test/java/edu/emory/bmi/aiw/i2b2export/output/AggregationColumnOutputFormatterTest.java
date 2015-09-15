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
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfiguration;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfiguration;
import edu.emory.bmi.aiw.i2b2export.i2b2.I2b2CommUtil;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Event;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Observation;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Patient;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class AggregationColumnOutputFormatterTest extends AbstractColumnFormatterTest {

	private OutputColumnConfiguration colConfig;
	private FormatOptions formatOptions;
	
	public AggregationColumnOutputFormatterTest() throws ParseException {
		final DateFormat i2b2DateFormat = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);

		colConfig = new OutputColumnConfiguration();
		colConfig.setColumnOrder(0);
		colConfig.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.AGGREGATION);

		OutputConfiguration config = new OutputConfiguration();
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

		Collection<Observation> obxs = new ArrayList<>();
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-01-01T09:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-01-01T10:00:00.000-0500")).nval("100.17").units("U").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-02-02T10:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-02-02T11:00:00.000-0500")).nval("200.28").units("U").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-03-03T11:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-03-03T12:00:00.000-0500")).nval("300.39").units("U").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-04-04T12:00:00.000-0400")).endDate(i2b2DateFormat.parse("2013-04-04T13:00:00.000-0400")).nval("400.40").units("U").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-05-05T13:00:00.000-0400")).endDate(i2b2DateFormat.parse("2013-05-05T14:00:00.000-0400")).nval("500.51").units("U").build());
		setObxs(obxs);
	}

	@Test
	public void testFormatMin() throws IOException {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.MIN);
		colConfig.setIncludeUnits(false);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);
		Assert.assertEquals("\"100.17\"", formatString(formatter));
	}

	@Test
	public void testFormatMax() throws IOException {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.MAX);
		colConfig.setIncludeUnits(false);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);
		Assert.assertEquals("\"500.51\"", formatString(formatter));
	}

	@Test
	public void testFormatAvg() throws IOException {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.AVG);
		colConfig.setIncludeUnits(false);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);
		Assert.assertEquals("\"300.35\"", formatString(formatter));
	}

	@Test
	public void testFormatMinUnits() throws IOException {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.MIN);
		colConfig.setIncludeUnits(true);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);

		Assert.assertEquals("\"100.17\",\"U\"", formatString(formatter));
	}

	@Test
	public void testFormatMaxUnits() throws IOException {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.MAX);
		colConfig.setIncludeUnits(true);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);

		Assert.assertEquals("\"500.51\",\"U\"", formatString(formatter));
	}

	@Test
	public void testFormatAvgUnits() throws IOException {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.AVG);
		colConfig.setIncludeUnits(true);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);

		Assert.assertEquals("\"300.35\",\"U\"", formatString(formatter));
	}
	
}

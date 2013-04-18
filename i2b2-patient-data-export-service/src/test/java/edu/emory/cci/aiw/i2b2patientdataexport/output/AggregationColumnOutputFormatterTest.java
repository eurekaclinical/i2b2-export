package edu.emory.cci.aiw.i2b2patientdataexport.output;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2CommUtil;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Event;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Observation;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Patient;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AggregationColumnOutputFormatterTest {
	private OutputConfiguration config;
	private OutputColumnConfiguration colConfig;
	private FormatOptions formatOptions;
	private Collection<Observation> obxs = new ArrayList<Observation>();

	public AggregationColumnOutputFormatterTest() throws ParseException {
		final DateFormat i2b2DateFormat = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);

		colConfig = new OutputColumnConfiguration();
		colConfig.setColumnOrder(0);
		colConfig.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.AGGREGATION);

		config = new OutputConfiguration();
		config.setName("foo");
		config.setUsername("i2b2");
		config.setRowDimension(OutputConfiguration.RowDimension.PATIENT);
		config.setSeparator(",");
        config.setQuoteChar("\"");
		config.setMissingValue("(NULL)");
		config.setWhitespaceReplacement("_");
		config.setColumnConfigs(new ArrayList<OutputColumnConfiguration>());

		formatOptions = new FormatOptions(config);

		Patient p = new Patient.Builder("1").build();
		Event e = new Event.Builder("1", p).build();

		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-01-01T09:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-01-01T10:00:00.000-0500")).nval("100.17").units("U").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-02-02T10:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-02-02T11:00:00.000-0500")).nval("200.28").units("U").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-03-03T11:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-03-03T12:00:00.000-0500")).nval("300.39").units("U").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-04-04T12:00:00.000-0400")).endDate(i2b2DateFormat.parse("2013-04-04T13:00:00.000-0400")).nval("400.40").units("U").build());
		obxs.add(new Observation.Builder(e).conceptPath("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-05-05T13:00:00.000-0400")).endDate(i2b2DateFormat.parse("2013-05-05T14:00:00.000-0400")).nval("500.51").units("U").build());
	}

	@Test
	public void testFormatMin() {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.MIN);
		colConfig.setIncludeUnits(false);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);

		Assert.assertEquals(Collections.singletonList("100.17"), formatter.format(obxs));
	}

	@Test
	public void testFormatMax() {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.MAX);
		colConfig.setIncludeUnits(false);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);

		Assert.assertEquals(Collections.singletonList("500.51"), formatter.format(obxs));
	}

	@Test
	public void testFormatAvg() {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.AVG);
		colConfig.setIncludeUnits(false);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);

		Assert.assertEquals(Collections.singletonList("300.35"), formatter.format(obxs));
	}

	@Test
	public void testFormatMinUnits() {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.MIN);
		colConfig.setIncludeUnits(true);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);

        List<String> expected = new ArrayList<String>();
        expected.add("100.17");
        expected.add("U");
		Assert.assertEquals(expected, formatter.format(obxs));
	}

	@Test
	public void testFormatMaxUnits() {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.MAX);
		colConfig.setIncludeUnits(true);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);

        List<String> expected = new ArrayList<String>();
        expected.add("500.51");
        expected.add("U");
		Assert.assertEquals(expected, formatter.format(obxs));
	}

	@Test
	public void testFormatAvgUnits() {
		colConfig.setAggregation(OutputColumnConfiguration.AggregationType.AVG);
		colConfig.setIncludeUnits(true);

		AggregationColumnOutputFormatter formatter = new AggregationColumnOutputFormatter(colConfig, formatOptions);

        List<String> expected = new ArrayList<String>();
        expected.add("300.35");
        expected.add("U");
		Assert.assertEquals(expected, formatter.format(obxs));
	}
}

package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2CommUtil;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Event;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observation;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observer;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Patient;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProviderDataRowOutputFormatterTest {

	private OutputConfiguration config;
	private Observer provider;

	public ProviderDataRowOutputFormatterTest() throws ParseException {
		DateFormat fmt = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);

		config = new OutputConfiguration();
		config.setUsername("i2b2");
		config.setName("foo");
		config.setRowDimension(OutputConfiguration.RowDimension.PATIENT);
		config.setMissingValue("(NULL)");
		config.setSeparator(",");
		config.setWhitespaceReplacement("_");
		config.setColumnConfigs(new ArrayList<OutputColumnConfiguration>());

		Patient patient = new Patient.Builder("P1").build();
		Event e1 = new Event.Builder("E1", patient).startDate(fmt.parse("2013-01-01T09:00:00.000-0500")).endDate(fmt.parse("2013-01-01T14:00:00.000-0500")).build();
		Event e2 = new Event.Builder("E2", patient).startDate(fmt.parse("2013-02-02T09:00:00.000-0500")).endDate(fmt.parse("2013-02-02T14:00:00.000-0500")).build();
		Event e3 = new Event.Builder("E3", patient).startDate(fmt.parse("2013-03-03T09:00:00.000-0500")).endDate(fmt.parse("2013-03-03T14:00:00.000-0500")).build();

		provider = new Observer.Builder("\\i2b2INTERNAL|Provider\\i2b2INTERNAL|Provider|S\\i2b2INTERNAL|Provider:SMITH, JOHN\\", "i2b2INTERNAL|Provider:SMITH, JOHN").name("SMITH, JOHN").build();

		provider.addObservation(new Observation.Builder(e1).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept1").startDate(fmt.parse
				("2013-01-01T09:00:00.000-0500")).endDate(fmt.parse
				("2013-01-01T10:00:00.000-0500")).tval("100").nval("100")
				.valuetype("N")
				.units("U").build());
		provider.addObservation(new Observation.Builder(e2).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept1").startDate(fmt.parse
				("2013-02-02T10:00:00.000-0500")).endDate(fmt.parse
				("2013-02-02T11:00:00.000-0500")).tval("200").nval("200")
				.valuetype("N")
				.units("U").build());
		provider.addObservation(new Observation.Builder(e3).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept1").startDate(fmt.parse
				("2013-03-03T11:00:00.000-0500")).endDate(fmt.parse
				("2013-03-03T12:00:00.000-0500")).tval("300").nval("300")
				.valuetype("N")
				.units("U").build());
		provider.addObservation(new Observation.Builder(e1).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept1").startDate(fmt.parse
				("2013-01-01T12:00:00.000-0500")).endDate(fmt.parse
				("2013-04-04T13:00:00.000-0500")).tval("400").nval("400")
				.valuetype("N")
				.units("U").build());
		provider.addObservation(new Observation.Builder(e2).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept1").startDate(fmt.parse
				("2013-02-02T13:00:00.000-0500")).endDate(fmt.parse
				("2013-05-05T14:00:00.000-0500")).tval("500").nval("500")
				.valuetype("N")
				.units("U").build());
		provider.addObservation(new Observation.Builder(e3).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept2").startDate(fmt.parse
				("2013-03-03T10:00:00.000-0500")).endDate(fmt.parse
				("2013-03-03T09:05:00.000-0500")).tval("1.0").nval("1.0")
				.valuetype("N")
				.units("X").build());
		provider.addObservation(new Observation.Builder(e1).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept2").startDate(fmt.parse
				("2013-01-01T09:00:00.000-0500")).endDate(fmt.parse
				("2013-01-01T09:05:00.000-0500")).tval("1.5").nval("1.5")
				.valuetype("N")
				.units("X").build());
		provider.addObservation(new Observation.Builder(e2).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept2").startDate(fmt.parse
				("2013-02-02T09:00:00.000-0500")).endDate(fmt.parse
				("2013-02-02T09:05:00.000-0500")).tval("1.8").nval("1.8")
				.valuetype("N")
				.units("X").build());
		provider.addObservation(new Observation.Builder(e3).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept2").startDate(fmt.parse
				("2013-03-03T09:00:00.000-0500")).endDate(fmt.parse
				("2013-03-03T09:05:00.000-0500")).tval("1.75").nval("1.75")
				.valuetype("N").units("X").build());
		provider.addObservation(new Observation.Builder(e1).conceptPath("\\\\i2b2\\Concepts\\MyConcept3").startDate(fmt.parse("2013-01-01T09:00:00.000-0500")).endDate(fmt.parse("2013-01-01T10:00:00.000-0500")).build());

		patient.addEvent(e1);
		patient.addEvent(e2);
		patient.addEvent(e3);

		OutputColumnConfiguration colConfig1 = new OutputColumnConfiguration();
		colConfig1.setColumnOrder(1);
		I2b2Concept concept1 = new I2b2Concept
				("\\\\i2b2\\Concepts\\MyConcept3", 2, "concept_dimension",
						"MyConcept3", "N");
		colConfig1.setI2b2Concept(concept1);
		colConfig1.setColumnName("MyConcept3");
		colConfig1.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.EXISTENCE);
		config.getColumnConfigs().add(colConfig1);

		OutputColumnConfiguration colConfig2 = new OutputColumnConfiguration();
		colConfig2.setColumnOrder(2);
		I2b2Concept concept2 = new I2b2Concept
				("\\\\i2b2\\Concepts\\MyConcept2", 2, "concept_dimension",
						"MyConcept2", "N");
		colConfig2.setI2b2Concept(concept2);
		colConfig2.setColumnName("MyConcept2");
		colConfig2.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);
		colConfig2.setHowMany(5);
		colConfig2.setIncludeTimeRange(true);
		colConfig2.setIncludeUnits(false);
		config.getColumnConfigs().add(colConfig2);

		OutputColumnConfiguration colConfig3 = new OutputColumnConfiguration();
		colConfig3.setColumnOrder(3);
		I2b2Concept concept3 = new I2b2Concept
				("\\\\i2b2\\Concepts\\MyConcept1", 2, "concept_dimension",
						"MyConcept1", "N");
		colConfig3.setI2b2Concept(concept3);
		colConfig3.setColumnName("MyConcept1");
		colConfig3.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.AGGREGATION);
		colConfig3.setAggregation(OutputColumnConfiguration.AggregationType.MAX);
		colConfig3.setIncludeUnits(true);
		config.getColumnConfigs().add(colConfig3);
	}

	@Test
	public void testFormat() {
		ProviderDataRowOutputFormatter formatter = new ProviderDataRowOutputFormatter(config, provider);
		Assert.assertEquals("SMITH, JOHN,true,1.0," +
				"2013-03-03T10:00:00.000-0500,2013-03-03T09:05:00.000-0500," +
				"1.75,2013-03-03T09:00:00.000-0500,2013-03-03T09:05:00.000-0500,1.8,2013-02-02T09:00:00.000-0500,2013-02-02T09:05:00.000-0500,1.5,2013-01-01T09:00:00.000-0500,2013-01-01T09:05:00.000-0500,(NULL),(NULL),(NULL),500,U", formatter.format());
	}
}

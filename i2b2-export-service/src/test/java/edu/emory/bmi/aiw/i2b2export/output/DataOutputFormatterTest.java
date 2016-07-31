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

import edu.emory.bmi.aiw.i2b2export.entity.AggregationType;
import edu.emory.bmi.aiw.i2b2export.entity.DisplayFormat;
import edu.emory.bmi.aiw.i2b2export.entity.I2b2ConceptEntity;
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.RowDimension;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import org.eurekaclinical.i2b2.client.I2b2CommUtil;
import org.eurekaclinical.i2b2.client.pdo.Event;
import org.eurekaclinical.i2b2.client.pdo.I2b2PdoResults;
import org.eurekaclinical.i2b2.client.pdo.Observation;
import org.eurekaclinical.i2b2.client.pdo.Observer;
import org.eurekaclinical.i2b2.client.pdo.Patient;

public class DataOutputFormatterTest extends AbstractRowOutputFormatterTest {

	private final OutputConfigurationEntity config;
	private final I2b2PdoResults data;
	private DataOutputFormatter formatter;

	public DataOutputFormatterTest() throws ParseException {
		DateFormat fmt = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);

		config = new OutputConfigurationEntity();
		config.setUsername("i2b2");
		config.setName("foo");
		config.setMissingValue("(NULL)");
		config.setSeparator(",");
		config.setQuoteChar("\"");
		config.setWhitespaceReplacement("_");
		config.setColumnConfigs(new ArrayList<OutputColumnConfigurationEntity>());

		Collection<Patient> patients = new ArrayList<>();
		Patient p1 = new Patient.Builder("P1").build();
		Patient p2 = new Patient.Builder("P2").build();
		Patient p3 = new Patient.Builder("P3").build();

		patients.add(p1);
		patients.add(p2);
		patients.add(p3);

		Collection<Event> visits = new ArrayList<>();
		// patient 1
		Event e11 = new Event.Builder("E11", p1).startDate(fmt.parse("2013-01-01T09:00:00.000-0500")).endDate(fmt.parse("2013-01-01T14:00:00.000-0500")).build();
		Event e12 = new Event.Builder("E12", p1).startDate(fmt.parse("2013-02-02T09:00:00.000-0500")).endDate(fmt.parse("2013-02-02T14:00:00.000-0500")).build();
		Event e13 = new Event.Builder("E13", p1).startDate(fmt.parse("2013-03-03T09:00:00.000-0500")).endDate(fmt.parse("2013-03-03T14:00:00.000-0500")).build();

		visits.add(e11);
		visits.add(e12);
		visits.add(e13);

		p1.addEvent(e11);
		p1.addEvent(e12);
		p1.addEvent(e13);

		// patient 2
		Event e21 = new Event.Builder("E21", p2).startDate(fmt.parse("2013-04-04T09:00:00.000-0400")).endDate(fmt.parse("2013-04-04T14:00:00.000-0400")).build();
		Event e22 = new Event.Builder("E22", p2).startDate(fmt.parse("2013-05-05T09:00:00.000-0400")).endDate(fmt.parse("2013-05-05T14:00:00.000-0400")).build();
		Event e23 = new Event.Builder("E23", p2).startDate(fmt.parse("2013-06-06T09:00:00.000-0400")).endDate(fmt.parse("2013-06-06T14:00:00.000-0400")).build();

		visits.add(e21);
		visits.add(e22);
		visits.add(e23);

		p2.addEvent(e21);
		p2.addEvent(e22);
		p2.addEvent(e23);

		// patient 3
		Event e31 = new Event.Builder("E31", p3).startDate(fmt.parse("2013-07-07T09:00:00.000-0400")).endDate(fmt.parse("2013-07-07T14:00:00.000-0400")).build();

		visits.add(e31);

		p3.addEvent(e31);

		Collection<Observer> providers = new ArrayList<>();

		Observer o1 = new Observer.Builder("\\i2b2INTERNAL|Provider\\i2b2INTERNAL|Provider|S\\i2b2INTERNAL|Provider:SMITH, JOHN", "i2b2INTERNAL|Provider:SMITH, JOHN").name("SMITH, JOHN").build();
		Observer o2 = new Observer.Builder("\\i2b2INTERNAL|Provider\\i2b2INTERNAL|Provider|D\\i2b2INTERNAL|Provider:DOE, JANE", "i2b2INTERNAL|Provider:DOE, JANE").name("DOE, JANE").build();
		Observer o3 = new Observer.Builder("\\i2b2INTERNAL|Provider\\i2b2INTERNAL|Provider|S\\i2b2INTERNAL|Provider:SALK, JONAS", "i2b2INTERNAL|Provider:SALK, JONAS").name("SALK, JONAS").build();
		Observer o4 = new Observer.Builder("\\i2b2INTERNAL|Provider\\i2b2INTERNAL|Provider|H\\i2b2INTERNAL|Provider:HIPPOCRATES, BRIAN", "i2b2INTERAL|Provider:HIPPOCRATES, BRIAN").name("HIPPOCRATES, BRIAN").build();

		providers.add(o1);
		providers.add(o2);
		providers.add(o3);
		providers.add(o4);

		Collection<Observation> observations = new ArrayList<>();

		// patient 1
		Observation obx1 = new Observation.Builder(e11).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept1").startDate(fmt.parse
				("2013-01-01T09:00:00.000-0500")).endDate(fmt.parse
				("2013-01-01T10:00:00.000-0500")).tval("100").nval("100")
				.valuetype("N")
				.units("U").build();
		Observation obx2 = new Observation.Builder(e12).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept1").startDate(fmt.parse
				("2013-02-02T09:00:00.000-0500")).endDate(fmt.parse
				("2013-02-02T10:00:00.000-0500")).tval("200").nval("200")
				.valuetype("N")
				.units("U").build();
		Observation obx3 = new Observation.Builder(e13).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept1").startDate(fmt.parse
				("2013-03-03T09:00:00.000-0500")).endDate(fmt.parse
				("2013-03-03T10:00:00.000-0500")).tval("300").nval("300")
				.valuetype("N")
				.units("U").build();
		Observation obx4 = new Observation.Builder(e13).conceptPath("\\\\i2b2\\Concepts\\MyConcept3").startDate(fmt.parse("2013-03-03T09:00:00.000-0500")).endDate(fmt.parse("2013-03-03T10:00:00.000-0500")).build();

		e11.addObservation(obx1);
		e12.addObservation(obx2);
		e13.addObservation(obx3);
		e13.addObservation(obx4);

		o1.addObservation(obx1);
		o1.addObservation(obx2);
		o2.addObservation(obx3);
		o1.addObservation(obx4);

		// patient 2
		Observation obx5 = new Observation.Builder(e21).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept2").startDate(fmt.parse
				("2013-04-04T09:00:00.000-0400")).endDate(fmt.parse
				("2013-04-04T09:05:00.000-0400")).tval("1.0").nval("1.0")
				.valuetype("N")
				.units("X").build();
		Observation obx6 = new Observation.Builder(e22).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept2").startDate(fmt.parse
				("2013-05-05T09:00:00.000-0400")).endDate(fmt.parse
				("2013-05-05T09:05:00.000-0400")).tval("1.5").nval("1.5")
				.valuetype("N")
				.units("X").build();
		Observation obx7 = new Observation.Builder(e22).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept2").startDate(fmt.parse
				("2013-06-06T09:00:00.000-0400")).endDate(fmt.parse
				("2013-06-06T09:05:00.000-0400")).tval("1.8").nval("1.8")
				.valuetype("N")
				.units("X").build();
		Observation obx8 = new Observation.Builder(e23).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept2").startDate(fmt.parse
				("2013-04-04T09:00:00.000-0400")).endDate(fmt.parse
				("2013-04-04T09:05:00.000-0400")).tval("1.75").nval("1.75")
				.valuetype("N").units("X").build();

		e21.addObservation(obx5);
		e22.addObservation(obx6);
		e22.addObservation(obx7);
		e23.addObservation(obx8);

		o3.addObservation(obx5);
		o3.addObservation(obx6);
		o3.addObservation(obx7);
		o1.addObservation(obx8);

		// patient 3
		Observation obx9 = new Observation.Builder(e31).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept4").startDate(fmt.parse
				("2013-07-07T09:00:00.000-0400")).endDate(fmt.parse
				("2013-07-07T10:00:00.000-0400")).tval("140").nval("140")
				.valuetype("N")
				.units("mm Hg").build();
		Observation obxA = new Observation.Builder(e31).conceptPath
				("\\\\i2b2\\Concepts\\MyConcept5").startDate(fmt.parse
				("2013-07-07T09:00:00.000-0400")).endDate(fmt.parse
				("2013-07-07T10:00:00.000-0400")).tval("90").nval("90")
				.valuetype("N").units("mm Hg").build();
		Observation obxB = new Observation.Builder(e31).conceptPath("\\\\i2b2\\Concepts\\MyConcept3").startDate(fmt.parse("2013-07-07T09:00:00.000-0400")).endDate(fmt.parse("2013-07-07T10:00:00.000-0400")).build();

		e31.addObservation(obx9);
		e31.addObservation(obxA);
		e31.addObservation(obxB);

		o4.addObservation(obx9);
		o4.addObservation(obxA);
		o4.addObservation(obxB);

		observations.add(obx1);
		observations.add(obx2);
		observations.add(obx3);
		observations.add(obx4);
		observations.add(obx5);
		observations.add(obx6);
		observations.add(obx7);
		observations.add(obx8);
		observations.add(obx9);
		observations.add(obxA);
		observations.add(obxB);

		data = new I2b2PdoResults(patients, visits, providers, observations);

		OutputColumnConfigurationEntity colConfig1 = new OutputColumnConfigurationEntity();
		colConfig1.setOutputConfig(config);
		colConfig1.setColumnOrder(1);
		I2b2ConceptEntity concept1 = new I2b2ConceptEntity
				("\\\\i2b2\\Concepts\\MyConcept3", 2, "concept_dimension",
						"MyConcept3", "N");
		colConfig1.setI2b2Concept(concept1);
		colConfig1.setColumnName("Concept 3");
		colConfig1.setDisplayFormat(DisplayFormat.EXISTENCE);

		OutputColumnConfigurationEntity colConfig2 = new OutputColumnConfigurationEntity();
		colConfig2.setOutputConfig(config);
		colConfig2.setColumnOrder(2);
		I2b2ConceptEntity concept2 = new I2b2ConceptEntity
				("\\\\i2b2\\Concepts\\MyConcept1", 2, "concept_dimension",
						"MyConcept1", "N");
		colConfig2.setI2b2Concept(concept2);
		colConfig2.setColumnName("Concept 1");
		colConfig2.setDisplayFormat(DisplayFormat.VALUE);
		colConfig2.setHowMany(3);
		colConfig2.setIncludeUnits(true);
		colConfig2.setIncludeTimeRange(false);

		OutputColumnConfigurationEntity colConfig3 = new OutputColumnConfigurationEntity();
		colConfig3.setOutputConfig(config);
		colConfig3.setColumnOrder(3);
		I2b2ConceptEntity concept3 = new I2b2ConceptEntity
				("\\\\i2b2\\Concepts\\MyConcept2", 2, "concept_dimension",
						"MyConcept2", "N");
		colConfig3.setI2b2Concept(concept3);
		colConfig3.setColumnName("Concept 2");
		colConfig3.setDisplayFormat(DisplayFormat.AGGREGATION);
		colConfig3.setAggregation(AggregationType.MAX);
		colConfig3.setIncludeUnits(true);

		OutputColumnConfigurationEntity colConfig4 = new OutputColumnConfigurationEntity();
		colConfig4.setOutputConfig(config);
		colConfig4.setColumnOrder(4);
		I2b2ConceptEntity concept4 = new I2b2ConceptEntity
				("\\\\i2b2\\Concepts\\MyConcept4", 2, "concept_dimension",
						"MyConcept4", "N");
		colConfig4.setI2b2Concept(concept4);
		colConfig4.setColumnName("Systolic");
		colConfig4.setDisplayFormat(DisplayFormat.VALUE);
		colConfig4.setIncludeTimeRange(true);
		colConfig4.setIncludeUnits(false);
		colConfig4.setHowMany(2);

		OutputColumnConfigurationEntity colConfig5 = new OutputColumnConfigurationEntity();
		colConfig5.setOutputConfig(config);
		colConfig5.setColumnOrder(5);
		I2b2ConceptEntity concept5 = new I2b2ConceptEntity
				("\\\\i2b2\\Concepts\\MyConcept5", 2, "concept_dimension",
						"MyConcept5", "N");
		colConfig5.setI2b2Concept(concept5);
		colConfig5.setColumnName("Diastolic");
		colConfig5.setDisplayFormat(DisplayFormat.VALUE);
		colConfig5.setIncludeTimeRange(true);
		colConfig5.setIncludeUnits(false);
		colConfig5.setHowMany(2);

		config.getColumnConfigs().add(colConfig1);
		config.getColumnConfigs().add(colConfig2);
		config.getColumnConfigs().add(colConfig3);
		config.getColumnConfigs().add(colConfig4);
		config.getColumnConfigs().add(colConfig5);
	}

	@Test
	public void testPatientOutputFormatter() throws IOException, SQLException {
		config.setRowDimension(RowDimension.PATIENT);
		formatter = new DataOutputFormatter(config, data);

		String expected = "\"Patient_id\",\"Concept_3\",\"Concept_1_value\",\"Concept_1_units\",\"Concept_1_value\",\"Concept_1_units\",\"Concept_1_value\",\"Concept_1_units\",\"Concept_2_max\",\"Concept_2_units\",\"Systolic_value\",\"Systolic_start\",\"Systolic_end\",\"Systolic_value\",\"Systolic_start\",\"Systolic_end\",\"Diastolic_value\",\"Diastolic_start\",\"Diastolic_end\",\"Diastolic_value\",\"Diastolic_start\",\"Diastolic_end\"\n" +
				"\"P1\",\"true\",\"300\",\"U\",\"200\",\"U\",\"100\",\"U\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n" +
				"\"P2\",\"false\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"1.8\",\"X\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n" +
				"\"P3\",\"true\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"140\",\"2013-07-07T09:00:00.000-0400\",\"2013-07-07T10:00:00.000-0400\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"90\",\"2013-07-07T09:00:00.000-0400\",\"2013-07-07T10:00:00.000-0400\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n";
		
		Assert.assertEquals(expected, formatString(formatter));
	}

	@Test
	public void testVisitOutputFormatter() throws IOException, SQLException {
		config.setRowDimension(RowDimension.VISIT);
		formatter = new DataOutputFormatter(config, data);

		String expected = "\"Patient_id\",\"Visit_id\",\"Visit_start\",\"Visit_end\",\"Concept_3\",\"Concept_1_value\",\"Concept_1_units\",\"Concept_1_value\",\"Concept_1_units\",\"Concept_1_value\",\"Concept_1_units\",\"Concept_2_max\",\"Concept_2_units\",\"Systolic_value\",\"Systolic_start\",\"Systolic_end\",\"Systolic_value\",\"Systolic_start\",\"Systolic_end\",\"Diastolic_value\",\"Diastolic_start\",\"Diastolic_end\",\"Diastolic_value\",\"Diastolic_start\",\"Diastolic_end\"\n" +
				"\"P1\",\"E11\",\"2013-01-01T09:00:00.000-0500\",\"2013-01-01T14:00:00.000-0500\",\"false\",\"100\",\"U\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n" +
				"\"P1\",\"E12\",\"2013-02-02T09:00:00.000-0500\",\"2013-02-02T14:00:00.000-0500\",\"false\",\"200\",\"U\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n" +
				"\"P1\",\"E13\",\"2013-03-03T09:00:00.000-0500\",\"2013-03-03T14:00:00.000-0500\",\"true\",\"300\",\"U\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n" +
				"\"P2\",\"E21\",\"2013-04-04T09:00:00.000-0400\",\"2013-04-04T14:00:00.000-0400\",\"false\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"1.0\",\"X\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n" +
				"\"P2\",\"E22\",\"2013-05-05T09:00:00.000-0400\",\"2013-05-05T14:00:00.000-0400\",\"false\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"1.8\",\"X\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n" +
				"\"P2\",\"E23\",\"2013-06-06T09:00:00.000-0400\",\"2013-06-06T14:00:00.000-0400\",\"false\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"1.75\",\"X\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n" +
				"\"P3\",\"E31\",\"2013-07-07T09:00:00.000-0400\",\"2013-07-07T14:00:00.000-0400\",\"true\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"140\",\"2013-07-07T09:00:00.000-0400\",\"2013-07-07T10:00:00.000-0400\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"90\",\"2013-07-07T09:00:00.000-0400\",\"2013-07-07T10:00:00.000-0400\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n";
		Assert.assertEquals(expected, formatString(formatter));
	}

	@Test
	public void testProviderOutputFormatter() throws IOException, SQLException {
		config.setRowDimension(RowDimension.PROVIDER);
		formatter = new DataOutputFormatter(config, data);

		String expected = "\"Provider_name\",\"Concept_3\",\"Concept_1_value\",\"Concept_1_units\",\"Concept_1_value\",\"Concept_1_units\",\"Concept_1_value\",\"Concept_1_units\",\"Concept_2_max\",\"Concept_2_units\",\"Systolic_value\",\"Systolic_start\",\"Systolic_end\",\"Systolic_value\",\"Systolic_start\",\"Systolic_end\",\"Diastolic_value\",\"Diastolic_start\",\"Diastolic_end\",\"Diastolic_value\",\"Diastolic_start\",\"Diastolic_end\"\n" +
				"\"SMITH, JOHN\",\"true\",\"200\",\"U\",\"100\",\"U\",\"(NULL)\",\"(NULL)\",\"1.75\",\"X\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n" +
				"\"DOE, JANE\",\"false\",\"300\",\"U\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n" +
				"\"SALK, JONAS\",\"false\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"1.8\",\"X\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n" +
				"\"HIPPOCRATES, BRIAN\",\"true\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"140\",\"2013-07-07T09:00:00.000-0400\",\"2013-07-07T10:00:00.000-0400\",\"(NULL)\",\"(NULL)\",\"(NULL)\",\"90\",\"2013-07-07T09:00:00.000-0400\",\"2013-07-07T10:00:00.000-0400\",\"(NULL)\",\"(NULL)\",\"(NULL)\"\n";
		Assert.assertEquals(expected, formatString(formatter));
	}
}

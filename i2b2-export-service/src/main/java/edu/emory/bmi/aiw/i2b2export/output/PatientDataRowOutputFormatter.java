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
import edu.emory.bmi.aiw.i2b2export.entity.I2b2Concept;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfiguration;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Event;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Observation;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Patient;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Output formatter for a patient row.
 *
 * @author Michel Mansour
 * @since 1.0
 */
final class PatientDataRowOutputFormatter extends DataRowOutputFormatter {

	private final Patient patient;
	private final Map<String, List<Observation>> keyToObx;

	PatientDataRowOutputFormatter(OutputConfiguration config, Patient patient, Connection con) {
		super(con, config);
		this.patient = patient;
		this.keyToObx = new HashMap<>();
		for (Event e : patient.getEvents()) {
			for (Observation o : e.getObservations()) {
				org.arp.javautil.collections.Collections.putList(this.keyToObx, o.getConceptPath(), o);
			}
		}
	}

	@Override
	protected int rowPrefix(BufferedWriter writer) throws IOException {
		write(patient.getPatientId(), writer, 0);
		return 1;
	}

	@Override
	protected Collection<Observation> matchingObservations(I2b2Concept i2b2Concept) throws SQLException {
		switch (StringUtils.upperCase(i2b2Concept.getTableName())) {
			case "CONCEPT_DIMENSION":
				List<Observation> obxs = this.keyToObx.get(i2b2Concept.getI2b2Key());
				if (obxs != null) {
					return Collections.unmodifiableCollection(obxs);
				} else {
					return Collections.emptyList();
				}
			case "PATIENT_DIMENSION":
				if (compareDimensionColumnValue(i2b2Concept, patient)) {
					Observation.Builder b = new Observation.Builder(null)
							.tval(getParam(patient, i2b2Concept));
					Collection<Observation> o = Collections.singleton(b.build());
					return o;
				} else {
					return Collections.emptyList();
				}
			default:
				return Collections.emptyList();
		}
	}

}

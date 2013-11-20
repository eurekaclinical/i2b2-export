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
import edu.emory.bmi.aiw.i2b2export.i2b2.I2b2CommUtil;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Event;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Observation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Output formatter for visit rows.
 *
 * @author Michel Mansour
 * @since 1.0
 */
final class VisitDataRowOutputFormatter extends DataRowOutputFormatter {
	private final Event visit;

	VisitDataRowOutputFormatter(OutputConfiguration config, Event visit) {
		super(config);
		this.visit = visit;
	}

	@Override
	public List<String> rowPrefix() {
		List<String> result = new ArrayList<>();
		DateFormat fmt = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);

		result.add(visit.getPatient().getPatientId());
		result.add(visit.getEventId());
		if (null != visit.getStartDate()) {
			result.add(fmt.format(visit.getStartDate()));
		} else {
			result.add("");
		}
		if (null != visit.getEndDate()) {
			result.add(fmt.format(visit.getEndDate()));
		} else {
			result.add("");
		}

		return result;
	}

	@Override
	protected Collection<Observation> matchingObservations(I2b2Concept i2b2Concept) {
		Collection<Observation> result = new ArrayList<>();

		for (Observation o : visit.getObservations()) {
			if (o.getConceptPath().equals(i2b2Concept.getI2b2Key())) {
				result.add(o);
			}
		}

		return result;
	}
}
